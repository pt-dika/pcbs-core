package dk.apps.pcps.main.module.tapcash.config;

import dk.apps.pcps.commonutils.ISOConverters;
import dk.apps.pcps.commonutils.Utility;
import dk.apps.pcps.config.auth.AuthService;
import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.TrxChannel;
import dk.apps.pcps.db.service.DeviceUserAndBatchGroupService;
import dk.apps.pcps.db.service.MerchantAndBatchGroupService;
import dk.apps.pcps.db.service.SettlementDataViewService;
import dk.apps.pcps.dbmaster.entity.MobileAppUsers;
import dk.apps.pcps.dbmaster.service.MobileAppUsersService;
import dk.apps.pcps.main.handler.MessageException;
import dk.apps.pcps.main.model.enums.CardEnum;
import dk.apps.pcps.main.module.tapcash.function.MACCalculation;
import dk.apps.pcps.main.module.tapcash.model.*;
import dk.apps.pcps.main.module.tapcash.model.enums.MTIActionEnum;

import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class HostService {
    AuthService authService;
    Channel channel;
    MobileAppUsersService mobileAppUsersService;
    DeviceUserAndBatchGroupService deviceUserAndBatchGroupService;
    MerchantAndBatchGroupService merchantAndBatchGroupService;
    SettlementDataViewService settlementDataViewService;

    @Autowired
    public HostService(AuthService authService,
                       Channel channel,
                       MobileAppUsersService mobileAppUsersService,
                       DeviceUserAndBatchGroupService deviceUserAndBatchGroupService,
                       MerchantAndBatchGroupService merchantAndBatchGroupService,
                       SettlementDataViewService settlementDataViewService
    ) {
        this.authService = authService;
        this.channel = channel;
        this.mobileAppUsersService = mobileAppUsersService;
        this.deviceUserAndBatchGroupService = deviceUserAndBatchGroupService;
        this.merchantAndBatchGroupService = merchantAndBatchGroupService;
        this.settlementDataViewService = settlementDataViewService;

    }

    public String getStan(String username) {
        return ISOConverters.padLeftZeros("" + mobileAppUsersService.getStan(username), 6);
    }

    public LogonData logonProcess(Messages msg, boolean isSettlement) throws ISOException, InterruptedException {
        CardEnum card = CardEnum.TAPCASH;
        String username = msg.getUsername();
        TrxChannel trxChannel = channel.getChannel(card);
        BatchGroup batchGroup = trxChannel.getBatchGroup();
        LogonData logonData = Utility.mapperToDto(deviceUserAndBatchGroupService.getAdditionalData(username, batchGroup), LogonData.class);
        String softwareVersion = "0100";
        //Logoff
        AdditionalData ad = new AdditionalData();
        if (isSettlement) {
            ad.setSoftwareVersion(softwareVersion);
            msg.setAdditionalData(ad);
            this.requestToHost(card, msg, MTIActionEnum.LOGOFF);
        }
        //Initialization
        ad = new AdditionalData();
        ad.setSoftwareVersion(softwareVersion);
        ad.setSeed(logonData.getSeed());
        ad.setCurrentBlacklistVersion("000000");
        ad.setMtmkIndex(logonData.getMtmkIndex());
        msg.setAdditionalData(ad);
        Messages msgInit = this.requestToHost(card, msg, MTIActionEnum.INITIALIZATION);
        String blacVersion = msgInit.getAdditionalData().getBlacklistVersionFrom();
        String paraVersion = msgInit.getAdditionalData().getParameterVersion();
        ad = msgInit.getAdditionalData();
        logonData.setRn(ad.getRn());
        logonData.setKek1(ad.getKek1());
        logonData.setMack(ad.getMack());
        deviceUserAndBatchGroupService.createAdditionalData(username, batchGroup, Utility.objectToString(logonData));
        if(logonData.getParameterVersion() == null || !logonData.getParameterVersion().equalsIgnoreCase(paraVersion)) {
            //Parameter
            ad = new AdditionalData();
            ad.setSoftwareVersion(softwareVersion);
            ad.setParameterVersion(paraVersion);
            ad.setFileName("PARA");
            ad.setRecordNumber("0000");
            msg.setAdditionalData(ad);
            Messages msgParam = this.requestToHost(card, msg, MTIActionEnum.PARAMETER);
            logonData.setParameterVersion(paraVersion);
            logonData.setParameterData(msgParam.getAdditionalData().getFileData());
        }
        if(logonData.getBlacklistVersion() == null || !logonData.getBlacklistVersion().equalsIgnoreCase(blacVersion)) {
            //Blacklist
            Messages msgBlac;
            String next;
            int i = 0;
            List<Object> lst = new ArrayList<>();
            AdditionalData additionalData = new AdditionalData();
            do {
                next = ISOConverters.padLeftZeros(""+ i * 10, 4);
                ad = new AdditionalData();
                ad.setSoftwareVersion(softwareVersion);
                ad.setBlacklistVersion(blacVersion);
                ad.setFileName("BLAC");
                ad.setRecordNumber(next);
                msg.setAdditionalData(ad);
                msgBlac = this.requestToHost(card, msg, MTIActionEnum.BLACKLIST);
                if (msgBlac != null) {
                    additionalData = msgBlac.getAdditionalData();
                    if (msgBlac.getAdditionalData() != null){
                        List<FileData> fileData = msgBlac.getAdditionalData().getFileData();
                        lst.addAll(fileData);
                    }
                }
                i++;
            } while (additionalData != null);

            BlacklistData blacklist = new BlacklistData();
            blacklist.setNextRecordNumber(next);
            blacklist.setFileData(lst);
            logonData.setBlacklistData(blacklist);
            logonData.setBlacklistVersion(blacVersion);
        }
        //Logon
        ad = new AdditionalData();
        ad.setSoftwareVersion(softwareVersion);
        msg.setAdditionalData(ad);
        this.requestToHost(card, msg, MTIActionEnum.LOGON);
        deviceUserAndBatchGroupService.createAdditionalData(username, batchGroup, Utility.objectToString(logonData));
        return logonData;
    }

    
    public Messages requestToHost(CardEnum card, Messages messagesRequest, MTIActionEnum mtiActionEnum) throws ISOException, InterruptedException {
        return requestToHost(card, messagesRequest, mtiActionEnum, false);
    }

    
    public Messages requestToHost(CardEnum card, Messages messagesRequest, MTIActionEnum mtiActionEnum, boolean isReversal) throws ISOException, InterruptedException {
        MobileAppUsers users = authService.getAuthUsers();
        String username = users.getUsername();
        TrxChannel trxChannel = channel.getChannel(card);
        channel.start(card);
        Object object = deviceUserAndBatchGroupService.getAdditionalData(username, trxChannel.getBatchGroup());
        LogonData logonData = Utility.mapperToDto(object, LogonData.class);
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setMTI(mtiActionEnum.mti);
        isoMsg.set(3, mtiActionEnum.processingCode); //Processing Code
        if (!isReversal)
            isoMsg.set(11, getStan(messagesRequest.getUsername())); //STAN
        if (messagesRequest.getChannel() != null)
            isoMsg.set(18, messagesRequest.getChannel()); //channel
        isoMsg.set(41, messagesRequest.getTID()); //TID
        isoMsg.set(42, messagesRequest.getMID()); //MID
        byte[] bAdditionalData = null;
        switch (mtiActionEnum) {
            case INITIALIZATION:
                bAdditionalData = ISOConverters.hexStringToBytes(messagesRequest.getAdditionalData().reqAdditionalDataInit());
                break;
            case PARAMETER:
                bAdditionalData = ISOConverters.hexStringToBytes(messagesRequest.getAdditionalData().reqAdditionalDataDownloadParameter());
                break;
            case BLACKLIST:
                bAdditionalData = ISOConverters.hexStringToBytes(messagesRequest.getAdditionalData().reqAdditionalDataDownloadBlacklist());
                break;
            case LOGON:
                bAdditionalData = ISOConverters.hexStringToBytes(messagesRequest.getAdditionalData().reqAdditionalDataLogon());
                break;
            case UN_MARRY_SAM:
                isoMsg.set(7, Utility.getDateTime());
                bAdditionalData = ISOConverters.hexToBytes(messagesRequest.getAdditionalData().reqAdditionalDataUnMarrySam());
                break;
            case CARD_UPDATE_REVERSAL:
                isoMsg.set(11, messagesRequest.getSTAN()); //STAN
            case CARD_UPDATE:
                isoMsg.set(2, messagesRequest.getPAN());
                isoMsg.set(7, Utility.getDateTime("MMddhhmmss"));
                isoMsg.set(12, Utility.getDateTime("hhmmss"));
                isoMsg.set(13, Utility.getDateTime("MMdd"));
                isoMsg.set(15, Utility.getDateTime("MMdd"));
                bAdditionalData = ISOConverters.hexToBytes(messagesRequest.getAdditionalData().reqAdditionalDataCardUpdate());
                break;
            case FILE_UPLOAD:
                Map map = messagesRequest.getAdditionalData().reqAdditionalDataSettlement();
                bAdditionalData = ISOConverters.hexStringToBytes(map.get("req_settlement").toString());
                break;
            case LOGOFF:
                bAdditionalData = ISOConverters.hexToBytes(messagesRequest.getAdditionalData().reqAdditionalDataLogoff());
                break;
            default:
                break;
        }
        isoMsg.set(48, bAdditionalData);
        byte[] bMac = new byte[]{
                0x00, 0x00,
                0x00, 0x00,
                0x00, 0x00,
                0x00, 0x00
        };
        isoMsg.set(64, bMac);
        byte[] msg = channel.msgToByte(isoMsg);
        byte[] mac = MACCalculation.getMAC3desCBC(logonData, msg);
        isoMsg.unset(64);
        isoMsg.set(64, mac);
        if (mtiActionEnum.equals(MTIActionEnum.INITIALIZATION))
            isoMsg.unset(64);
        ISOMsg response = channel.sendMessage(isoMsg);
        Messages messages = null;
        AdditionalData additionalData = null;
        if (response != null) {
            log.info(ISOConverters.logISOMsg(isoMsg, response, mtiActionEnum.name()));
            messages = new Messages();
            messages.setTrxChannel(trxChannel);
            messages.setMTI(response.getMTI());
            messages.setProcessingCode(response.getString(3));
            messages.setSTAN(response.getString(11));
            messages.setLocalTime(response.getString(12));
            messages.setLocalDate(response.getString(13));
            messages.setChannel(response.getString(18));
            messages.setResponseCode(response.getString(39));
            messages.setTID(response.getString(41));
            messages.setMID(response.getString(42));
            switch (mtiActionEnum) {
                case INITIALIZATION:
                    additionalData = new AdditionalData().respAdditionalDataInit(response.getString(48));
                    break;
                case PARAMETER:
                    additionalData = new AdditionalData().respAdditionalDataDownloadParameter(response.getString(48));
                    break;
                case BLACKLIST:
                    additionalData = messagesRequest.getAdditionalData().respAdditionalDataDownloadBlacklist(response.getString(48));
                    break;
                case LOGON:
                    messages.setMerchantName(response.getString(43));
                    break;
                case UN_MARRY_SAM:
                    additionalData = new AdditionalData().respAdditionalDataUnMarrySam(response.getString(48));
                    break;
                case CARD_UPDATE:
                    messages.setTransAmount(response.getString(4));
                    messages.setTransmissionDateTime(response.getString(7));
                    messages.setRRN(response.getString(37));
                    additionalData = new AdditionalData().respAdditionalDataCardUpdate(response.getString(48));
                    break;
                default:
                    break;
            }
            if (!messages.getResponseCode().equals("00")) {
                log.info(ISOConverters.logISOMsg(isoMsg, response, mtiActionEnum.name()));
                throw new MessageException(messages.getResponseCode());
            }
            messages.setAdditionalData(additionalData);
            messages.setMAC(response.getString(64));
            //channel.terminate();
        } else {
            log.info(ISOConverters.logISOMsg(isoMsg, mtiActionEnum.name()));
            throw new MessageException("0E");
        }
        return messages;
    }
}
