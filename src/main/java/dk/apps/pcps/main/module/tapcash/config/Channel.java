package dk.apps.pcps.main.module.tapcash.config;

import dk.apps.pcps.commonutils.Utility;
import dk.apps.pcps.db.entity.*;

import dk.apps.pcps.db.service.BinAndBatchGroupTemplateListService;
import dk.apps.pcps.db.service.MerchantDetailService;
import dk.apps.pcps.db.service.TrxChannelService;
import dk.apps.pcps.dbmaster.entity.MobileAppUsers;
import dk.apps.pcps.dbmaster.service.DeviceUserTleService;
import dk.apps.pcps.dbmaster.service.MobileAppUsersService;
import dk.apps.pcps.main.handler.ApplicationException;
import dk.apps.pcps.main.model.enums.CardEnum;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import dk.apps.pcps.model.iso8583.Iso8583Properties;
import dk.apps.pcps.model.result.ChannelResult;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.*;
import org.jpos.iso.channel.NACChannel;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

import static ch.qos.logback.core.encoder.ByteArrayUtil.hexStringToByteArray;

@Configuration
@Slf4j
public class Channel {

    ISOMUX isoMux;
    TrxChannelService trxChannelService;
    MobileAppUsersService mobileAppUsersService;
    MerchantDetailService merchantDetailService;
    BinAndBatchGroupTemplateListService binAndBatchGroupTemplateListService;
    DeviceUserTleService deviceUserTleService;
    Iso8583Properties iso8583Properties;

    @Autowired
    public Channel(Iso8583Properties iso8583Properties,
                   MobileAppUsersService mobileAppUsersService,
                   MerchantDetailService merchantDetailService,
                   BinAndBatchGroupTemplateListService binAndBatchGroupTemplateListService,
                   DeviceUserTleService deviceUserTleService,
                   TrxChannelService trxChannelService) {
        this.iso8583Properties = iso8583Properties;
        this.mobileAppUsersService = mobileAppUsersService;
        this.merchantDetailService = merchantDetailService;
        this.binAndBatchGroupTemplateListService = binAndBatchGroupTemplateListService;
        this.deviceUserTleService = deviceUserTleService;
        this.trxChannelService = trxChannelService;
    }

    public TrxChannel getChannel(CardEnum card) {
        TrxChannel trxChannel = trxChannelService.getTrxChannel(card.code);
        return trxChannel;
    }

    public ChannelResult getTrxChannel(String can, String username) {
        BinParent dBinParent = null;
        BatchGroup batchGroup = null;
        TrxChannel trxChannel = null;
        Integer binId = null;
        String binResultName = null;



        MobileAppUsers mobileAppUsers = mobileAppUsersService.getUser(username);
        MerchantDetail merchantDetail = merchantDetailService.getMerchantDetail(mobileAppUsers.getMerchant().getId());
        BinAndBatchGroupTemplateList binAndBatchGroupTemplate = binAndBatchGroupTemplateListService.getTemplateList(binId, merchantDetail.getBatchGroupListTemplate().getId());
        batchGroup = binAndBatchGroupTemplate.getBatchGroup();
        trxChannel = trxChannelService.getTrxChannel(batchGroup.getId());

        if (trxChannel == null)
            throw new ApplicationException(ProcessMessageEnum.NOT_FOUND, "card number ", can, " not define in channel,");

        return new ChannelResult()
                .setMobileAppUsers(mobileAppUsers)
                .setBinResultName(binResultName)
                .setBatchGroup(batchGroup)
                .setTrxChannel(trxChannel);
    }

    public void start(CardEnum card) {
        try {
            //Get trx_channel
            TrxChannel trxChannel = getChannel(card);
            String host = trxChannel.getHost();
            int port = trxChannel.getPort();
            String nii = trxChannel.getNii();
            log.info("Open Connection from "+host+":"+port);
            log.info("Header: "+nii);
            //Check trx_channel result
            if (!host.equals("")) {
                GenericPackager ISOPack = new GenericPackager(iso8583Properties.getPackager());
                byte[] TPDUba = hexStringToByteArray(getIsoHeader(nii));
                NACChannel NACChn = new NACChannel(host, port, ISOPack, TPDUba);
                if(isoMux == null){
                    isoMux = new ISOMUX(NACChn) {
                        protected String getKey(ISOMsg m) throws ISOException {
                            return super.getKey(m);
                        }
                    };
                    new Thread(isoMux).start();
                }
//                if(!isoMux.isConnected()){
//                    isoMux.getISOChannel().connect();
//                    new Thread(isoMux).start();
//                }
            } else {

            }
        } catch (ISOException e) {
            e.printStackTrace();
        }
    }

    public static String getIsoHeader(String nii) {
        StringBuffer headerBuff = new StringBuffer("6000000000");
        headerBuff.replace(3, 6, nii);
        String header = headerBuff.toString();
        return header;
    }

    public ISOMsg sendMessage(ISOMsg isoMsg) {
        ISORequest isoReq = new ISORequest(isoMsg);
        isoMux.queue(isoReq);
        ISOMsg response = isoReq.getResponse(10 * 1000);
        if (response != null) {
            byte[] msgResponse = msgToByte(response);
            System.out.println("Response Message: " + Utility.bytesToHex(msgResponse));
        }
        return response;
    }

    public void terminate() {
        isoMux.terminate();
    }

    public void setConnect(boolean isConnect) {
        isoMux.setConnect(isConnect);
    }

    public byte[] msgToByte(ISOMsg imPlainMsg) {

        ISOMsg imBuffMsg;

        try {
            ISOPackager isoPack = new GenericPackager(iso8583Properties.getPackager());
            imPlainMsg.setPackager(isoPack);
            imBuffMsg = imPlainMsg;
            byte[] baBuffMsg = imBuffMsg.pack();
            if (baBuffMsg[0] == 0) {
                byte[] tmp = new byte[baBuffMsg.length - 1];
                System.arraycopy(baBuffMsg, 1, tmp, 0, tmp.length);
                baBuffMsg = tmp;
            }
            baBuffMsg = Arrays.copyOfRange(baBuffMsg, 0, baBuffMsg.length);
            return baBuffMsg;
        } catch (ISOException e) {
            return null;
        }

    }

    public byte[] getMacBa() {
        byte[] mac_ba = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
        return mac_ba;
    }
}
