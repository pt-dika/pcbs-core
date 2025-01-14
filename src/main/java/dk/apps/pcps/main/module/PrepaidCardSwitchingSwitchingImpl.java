package dk.apps.pcps.main.module;

import dk.apps.pcps.commonutils.ImageUtils;
import dk.apps.pcps.config.PCPSConfig;
import dk.apps.pcps.config.auth.AuthService;
import dk.apps.pcps.db.entity.*;
import dk.apps.pcps.db.service.*;
import dk.apps.pcps.dbmaster.entity.Bank;
import dk.apps.pcps.dbmaster.entity.Merchant;
import dk.apps.pcps.dbmaster.entity.MobileAppUsers;
import dk.apps.pcps.dbmaster.service.BankService;
import dk.apps.pcps.dbmaster.service.MobileAppUsersService;
import dk.apps.pcps.main.model.enums.CardEnum;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import dk.apps.pcps.main.model.payload.ReqPayment;
import dk.apps.pcps.main.model.payload.ReqUpdateBalance;
import dk.apps.pcps.main.model.result.*;
import dk.apps.pcps.main.module.tapcash.model.*;
import dk.apps.pcps.main.handler.ApplicationException;
import dk.apps.pcps.main.module.brizzi.model.BrizziTrxData;
import dk.apps.pcps.main.module.emoney.EMoneyService;
import dk.apps.pcps.main.module.emoney.model.TrxData;
import dk.apps.pcps.main.module.flazz.model.FlazzTrx;
import dk.apps.pcps.main.module.tapcash.TapCashService;
import dk.apps.pcps.main.module.tapcash.model.enums.TransactionType;
import dk.apps.pcps.main.utils.ISOConverters;
import dk.apps.pcps.main.utils.ObjectMapper;
import dk.apps.pcps.main.utils.Utility;
import dk.apps.pcps.model.emoney.LoginPin;
import dk.apps.pcps.main.model.result.fileupload.SettlementFileUploadResult;
import dk.apps.pcps.validation.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
public class PrepaidCardSwitchingSwitchingImpl implements PrepaidCardSwitchingService {

    ValidationUtils validationUtils;
    AuthService authService;
    BankService bankService;
    BinChildListService binChildListService;
    BinParentListService binParentListService;
    MerchantDetailService merchantDetailService;
    BinAndBatchGroupTemplateListService binAndBatchGroupTemplateListService;
    TrxChannelService trxChannelService;
    BatchGroupService batchGroupService;
    MobileAppUsersService mobileAppUsersService;
    DeviceUserDetailService deviceUserDetailService;
    DeviceUserAndBatchGroupService deviceUserAndBatchGroupService;
    MerchantAndBatchGroupService merchantAndBatchGroupService;
    BatchGroupMdrService batchGroupMdrService;
    MerchantMdrService merchantMdrService;
    SuccessPaymentService successPaymentService;
    RefundService refundService;
    SettlementService settlementService;
    SettlementDataViewService settlementDataViewService;
    TrxHistoryService trxHistoryService;
    ReversalCardUpdateService reversalCardUpdateService;
    CardUpdateHistoryService cardUpdateHistoryService;

    SettlementFileUploadService settlementFileUploadService;
    TapCashService tapCashService;
    EMoneyService eMoneyService;

    @Autowired
    public PrepaidCardSwitchingSwitchingImpl(
            ValidationUtils validationUtils,
            BinChildListService binChildListService,
            AuthService authService,
            BankService bankService,
            BinParentListService binParentListService,
            BatchGroupService batchGroupService,
            TrxChannelService trxChannelService,
            MobileAppUsersService mobileAppUsersService,
            MerchantDetailService merchantDetailService,
            BinAndBatchGroupTemplateListService binAndBatchGroupTemplateListService,
            DeviceUserDetailService deviceUserDetailService,
            MerchantAndBatchGroupService merchantAndBatchGroupService,
            DeviceUserAndBatchGroupService deviceUserAndBatchGroupService,
            BatchGroupMdrService batchGroupMdrService,
            MerchantMdrService merchantMdrService,
            SuccessPaymentService successPaymentService,
            RefundService refundService,
            SettlementService settlementService,
            SettlementDataViewService settlementDataViewService,
            TrxHistoryService trxHistoryService,
            ReversalCardUpdateService reversalCardUpdateService,
            CardUpdateHistoryService cardUpdateHistoryService,
            SettlementFileUploadService settlementFileUploadService,
            TapCashService tapCashService,
            EMoneyService eMoneyService) {
        this.validationUtils = validationUtils;
        this.binChildListService = binChildListService;
        this.authService = authService;
        this.bankService = bankService;
        this.binParentListService = binParentListService;
        this.batchGroupService = batchGroupService;
        this.trxChannelService = trxChannelService;
        this.mobileAppUsersService = mobileAppUsersService;
        this.merchantDetailService = merchantDetailService;
        this.binAndBatchGroupTemplateListService = binAndBatchGroupTemplateListService;
        this.deviceUserDetailService = deviceUserDetailService;
        this.deviceUserAndBatchGroupService = deviceUserAndBatchGroupService;
        this.merchantAndBatchGroupService = merchantAndBatchGroupService;
        this.batchGroupMdrService = batchGroupMdrService;
        this.merchantMdrService = merchantMdrService;
        this.successPaymentService = successPaymentService;
        this.refundService = refundService;
        this.settlementService = settlementService;
        this.settlementDataViewService = settlementDataViewService;
        this.trxHistoryService = trxHistoryService;
        this.reversalCardUpdateService = reversalCardUpdateService;
        this.cardUpdateHistoryService = cardUpdateHistoryService;
        this.settlementFileUploadService = settlementFileUploadService;
        this.tapCashService = tapCashService;
        this.eMoneyService = eMoneyService;
    }

    private String getUsername(){
        MobileAppUsers appUser = authService.getAuthUsers();
        return appUser.getUsername();
    }

    private BatchGroup getBatchGroup(Integer batchGroupId){
        if (batchGroupId == null)
            return null;
        BatchGroup batchGroup = batchGroupService.getBatchGroup(batchGroupId);
        return batchGroup;
    }

    private String getPrepaidLogo(int batchGroupId) {
        BatchGroup batchGroup = batchGroupService.getBatchGroup(batchGroupId);
        BufferedImage bufferedImage = null;
        String parentDir = new File(System.getProperty("user.dir")).getParent();
        if (parentDir == null)
            parentDir = "/opt/tomcat/";
        log.info(parentDir);
        File file = new File(parentDir+PCPSConfig.LOGO_PATH+batchGroup.getName() + ".png");
        try {
            bufferedImage = ImageIO.read(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            throw new ApplicationException(ProcessMessageEnum.FAILED, e.getMessage());
        }
    }

    @Override
    public BankInfo getBankInfo(int batchGroupId) {
        BatchGroup batchGroup = getBatchGroup(batchGroupId);
        Bank bank = bankService.getBank(batchGroup.getBankId());
        String logo = bank.getPrintReceiptLogo();
        return new BankInfo()
                .setBankName(bank.getName())
                .setBankCode(bank.getCode())
                .setBankLogo(logo.split(",")[1])
                .setPrepaidName(batchGroup.getName().toUpperCase())
                .setPrepaidLogo(getPrepaidLogo(batchGroupId));
    }

    @Override
    public String getAllLogoPrepaid() {
        List<BufferedImage> bufferedImages = new ArrayList<>();
        List<BinAndBatchGroupTemplateList> templateList = binAndBatchGroupTemplateListService.getTemplateList();
        for (BinAndBatchGroupTemplateList u: templateList) {
            BufferedImage bufferedImage;
            String parentDir = new File(System.getProperty("user.dir")).getParent();
            if (parentDir == null)
                parentDir = "/opt/tomcat/";
            File file = new File(parentDir+PCPSConfig.LOGO_PATH+u.getBatchGroup().getName() + ".png");
            try {
                bufferedImage = ImageIO.read(file);
            } catch (IOException e) {
                continue;
            }
            bufferedImages.add(bufferedImage);
        }
        return Base64.getEncoder().encodeToString(ImageUtils.append(bufferedImages, "", false));
    }

    @Override
    public CardEnum getCard(Integer batchGroupId) {
        BatchGroup batchGroup = new BatchGroup();
        batchGroup.setId(batchGroupId);
        return CardEnum.getCardEnum(batchGroup.getId());
    }

    @Override
    public BinAllowResult getAidBinList() {
        MobileAppUsers mobileAppUsers = authService.getAuthUsers();
        String username = mobileAppUsers.getUsername();
        List<BinAllowData> binAllowData = new ArrayList<>();
        DeviceUserDetail deviceUserDetail = deviceUserDetailService.getUserDetail(username);
        int invoiceNumber = deviceUserDetail.getInvoiceNum() + 1;
        MerchantDetail merchantDetail = merchantDetailService.getMerchantDetail(mobileAppUsers.getMerchant().getId());
        List<BinAndBatchGroupTemplateList> binAndBatchGroupTemplateList = binAndBatchGroupTemplateListService.getTemplateList(merchantDetail.getBatchGroupListTemplate().getId());
        for (BinAndBatchGroupTemplateList bin : binAndBatchGroupTemplateList) {
            DeviceUserAndBatchGroup deviceUserAndBatchGroup = deviceUserAndBatchGroupService.getDeviceUserAndBatchGroup(username, bin.getBatchGroup());
            int batchNumber = deviceUserAndBatchGroup.getBatchNum() + 1;
            int id = bin.getBinId();
            Boolean binFlag = bin.getIsParentFlag();
            BatchGroup batchGroup = bin.getBatchGroup();
            String bankInfoUrl = "/config/bank-info/" + batchGroup.getId();
            DeviceUserAndBatchGroup deviceUser = deviceUserAndBatchGroupService.getDeviceUserAndBatchGroup(username, batchGroup);
            String samPairCode = null;
            CardEnum card = CardEnum.getCardEnum(batchGroup.getId());
            SamData samData = new SamData();
            switch (card) {
                case TAPCASH:
                    LogonData logonData = Utility.jsonToObject(deviceUser.getAdditionalParam(), LogonData.class);
                    if (logonData.getSamData() != null) {
                        //existing
                        //samPairCode = logonData.getSamData().getPairCode();
                        samData.setPairCode(logonData.getSamData().getPairCode());
                    }
                    break;
                case EMONEY:
                    LoginPin loginPin = Utility.jsonToObject(deviceUser.getAdditionalParam(), LoginPin.class);
                    if (loginPin != null) {
                        samData.setPinNo(loginPin.getPinNo());
                        samData.setPinCode(loginPin.getPinCode());
                    }
                    break;
                case BRIZZI:
                case JAKCARD:
            }
            MerchantAndBatchGroup merchantUser = merchantAndBatchGroupService.getMerchantAndBatchGroup(mobileAppUsers.getMerchant().getId(), batchGroup);
            if (binFlag) {
                BinParent binParent = binParentListService.getBinParent(id);
                binAllowData.add(new BinAllowData()
                        .setBatchGroupId(batchGroup.getId())
                        //.setSamPairCode(samPairCode)
                        .setTid(deviceUser.getTid())
                        .setMid(merchantUser.getMid())
                        .setBatchNum(ISOConverters.padLeftZeros(String.valueOf(batchNumber), 6))
                        .setAid(binParent.getAid())
                        .setBinName(binParent.getName())
                        .setBankInfoUrl(bankInfoUrl)
                        .setPrepaidCardName(batchGroup.getName().toUpperCase())
                        .setSamData(samData)
                );
            } else {
                BinChild binChild = binChildListService.getBinChild(id);
                binAllowData.add(new BinAllowData()
                        .setBatchGroupId(batchGroup.getId())
                        //.setSamPairCode(samPairCode)
                        .setTid(deviceUser.getTid())
                        .setMid(merchantUser.getMid())
                        .setBatchNum(ISOConverters.padLeftZeros(String.valueOf(batchNumber), 6))
                        .setAid(binChild.getAid())
                        .setBinName(binChild.getName())
                        .setBankInfoUrl(bankInfoUrl)
                        .setPrepaidCardName(batchGroup.getName().toUpperCase())
                        .setSamData(samData)
                );
            }
        }
        return new BinAllowResult()
                .setAidList(binAllowData)
                .setInvoiceNumber(ISOConverters.padLeftZeros(String.valueOf(invoiceNumber), 6));
    }

    @Override
    public TransactionResult doSaveTrx(ReqPayment req) {
        long lastBalance = 0;
        long currentBalance = 0;
        String baseAmount = "";
        String additionalData = null;
        String cardNumber = req.getCardNumber();
        Integer batchGroupId = req.getBatchGroupId();
        String deviceUser = req.getUserDevice();
        boolean isDeviceUser = deviceUser.equals(getUsername());
        String username = isDeviceUser?getUsername():deviceUser;
        BatchGroup batchGroup = getBatchGroup(batchGroupId);
        String binResultName = req.getBinResultName();
        MobileAppUsers appUsers = authService.getAuthUsers();
        if (!isDeviceUser)
            appUsers = mobileAppUsersService.getUser(username);
        String tid = req.getTid();
        CardEnum card = CardEnum.getCardEnum(batchGroupId);
        switch (card) {
            case TAPCASH:
                baseAmount = ISOConverters.twoComplementValue(req.getBaseAmount());
                CardData cardData = Utility.mapperToDto(req.getCardData(), CardData.class);
                validationUtils.validate(cardData);
                cardData.setTransType(TransactionType.PURCHASE.code);
                cardData.setDebitAmount(baseAmount);
                cardData.setTrp(tid);
                lastBalance = Long.parseLong(cardData.getPurseBalance(), 16);
                currentBalance = Long.parseLong(cardData.getLastPurseBalance(), 16);
                additionalData = Utility.objectToString(cardData);
                break;
            case EMONEY:
                TrxData trxData = Utility.mapperToDto(req.getCardData(), TrxData.class);
                validationUtils.validate(trxData);
                lastBalance = ISOConverters.convertBalanceEMoney(Utility.hexStringToByteArray(trxData.getLastBalance()));
                currentBalance = ISOConverters.convertBalanceEMoney(Utility.hexStringToByteArray(trxData.getCurrentBalance()));
                additionalData = Utility.objectToString(trxData);
                break;
            case BRIZZI:
                BrizziTrxData brizziTrxData = Utility.mapperToDto(req.getCardData(), BrizziTrxData.class);
                validationUtils.validate(brizziTrxData);
                lastBalance = ISOConverters.convertAmountBrizzi(Utility.hexStringToByteArray(brizziTrxData.getLastBalance()));
                currentBalance = ISOConverters.convertAmountBrizzi(Utility.hexStringToByteArray(brizziTrxData.getCurrentBalance()));
                additionalData = Utility.objectToString(brizziTrxData);
                break;
            case FLAZZ:
                FlazzTrx flazzTrxData = Utility.mapperToDto(req.getCardData(), FlazzTrx.class);
                validationUtils.validate(flazzTrxData);
                lastBalance = Long.parseLong(ISOConverters.padLeftZeros(flazzTrxData.getLastBalance(), 8), 16);
                currentBalance = Long.parseLong(ISOConverters.padLeftZeros(flazzTrxData.getCurrentBalance(), 8), 16);
                additionalData = Utility.objectToString(flazzTrxData);
                break;
            case JAKCARD:
            default:
                break;
        }

        boolean isGenerateInvoice = true;
        if (appUsers.getMerchant() == null)
            throw new ApplicationException(ProcessMessageEnum.MERCHANT_NOT_DEFINED);

        Merchant merchant = appUsers.getMerchant();
        int salesId = merchant.getSalesId();
        int merchantId = merchant.getId();
        DeviceUserDetail deviceUserDetail = deviceUserDetailService.getUserDetail(username);
        int invoiceNum = deviceUserDetail.getInvoiceNum() + 1;
        if (invoiceNum > 999999) {
            invoiceNum = 1;
        }
        int rInvoiceNum = Integer.parseInt(req.getInvoiceNum());
        if (rInvoiceNum < invoiceNum) {
            isGenerateInvoice = false;
            invoiceNum = rInvoiceNum;
        } else if (rInvoiceNum > invoiceNum) {
            invoiceNum = rInvoiceNum;
        }
        SuccessPaymentDetail result = successPaymentService.getSuccessPaymentDetail(rInvoiceNum, username);
        if (result != null)
            throw new ApplicationException(ProcessMessageEnum.ALREADY_PAID);
        MerchantAndBatchGroup merchantAndBatchGroup = merchantAndBatchGroupService.getMerchantAndBatchGroup(merchantId, batchGroup);
        String mid = merchantAndBatchGroup.getMid();
        DeviceUserAndBatchGroup deviceUserAndBatchGroup = deviceUserAndBatchGroupService.getDeviceUserAndBatchGroup(username, getBatchGroup(batchGroupId));
        tid = deviceUserAndBatchGroup.getTid();
        BatchGroupMdr batchGroupMdr = batchGroupMdrService.getBatchGroupMdr(batchGroup);
        Long batchGroupAdminFee = batchGroupMdr.getAdminFee();
        MerchantMdr merchantMdr = merchantMdrService.getMerchantMdr(merchantId, batchGroup);
        Long merchantMdrValue = merchantMdr.getMerchantMdr();
        Long salesMdrValue = merchantMdr.getSalesMdr();
        Long merchantAdminFee = merchantMdr.getMerchantAdminFee();
        Long salesAdminFee = merchantMdr.getSalesAdminFee();
        SuccessPayment successPayment = new SuccessPayment();
        successPayment.setAdditionalData(additionalData);
        successPayment.setPosRequestType(req.getPosRequestType());
        successPayment.setUsername(username);
        successPayment.setBatchGroup(batchGroup);
        successPayment.setMid(mid);
        successPayment.setTid(tid);
        successPayment.setTrxChannel(null);
        successPayment.setBaseAmount(req.getBaseAmount());
        successPayment.setCurrentBalance(currentBalance);
        successPayment.setLastBalance(lastBalance);
        successPayment.setBatchGroupMdrValue(batchGroupMdr.getMdr());
        successPayment.setMerchantMdrValue(merchantMdrValue);
        successPayment.setSalesMdrValue(salesMdrValue);
        successPayment.setInvoiceNum(invoiceNum);
        successPayment.setApprovalCode(req.getApprovalCode());
        successPayment.setFirstCopyFlag(false);
        successPayment.setPrintReceiptEmailFlag(false);
        successPayment.setPrintReceiptPhoneNumFlag(false);
        successPayment.setAmountKsnIndex("10");
        successPayment.setPaymentHostDate("12");
        successPayment.setPaymentHostTime("1220");
        successPayment.setBatchGroupAdminFee(batchGroupAdminFee);
        successPayment.setMerchantAdminFee(merchantAdminFee);
        successPayment.setSalesAdminFee(salesAdminFee);
        successPayment.setSalesId(salesId);
        successPayment.setCan(cardNumber);
        successPayment.setBinResultName(binResultName);
        result = successPaymentService.createTransaction(successPayment);
        if (result != null && isGenerateInvoice) {
            deviceUserDetailService.updateInvoiceNumber(username, invoiceNum);
        }
        MerchantData merchantData = ObjectMapper.map(merchant, MerchantData.class);
        return new TransactionResult().setTransaction(result).setMerchant(merchantData);
    }

    @Override
    public TransactionResult getLastTrx(Integer batchGroupId) {
        List<SuccessPaymentDetail> successPaymentDetails = successPaymentService.getSuccessPaymentDetails(getUsername(), getBatchGroup(batchGroupId), 1,1);
        MerchantData merchantData = ObjectMapper.map(authService.getAuthUsers().getMerchant(), MerchantData.class);
        SuccessPaymentDetail successPaymentDetail = ObjectMapper.map(successPaymentDetails.get(0), SuccessPaymentDetail.class);
        return new TransactionResult().setTransaction(successPaymentDetail).setMerchant(merchantData);
    }

    @Override
    public SuccessPaymentResult successPaymentList() {
        String username = authService.getAuthUsers().getUsername();
        SuccessPaymentResult successPaymentResult = new SuccessPaymentResult();
        MerchantData merchant = ObjectMapper.map(authService.getAuthUsers().getMerchant(), MerchantData.class);
        successPaymentResult.setMerchant(merchant);
        List<BatchGroup> batchGroups = successPaymentService.getBatchGroup(username);
        if (batchGroups.size() == 0)
            throw new ApplicationException(ProcessMessageEnum.NOT_FOUND, "success data");
        List<BatchGroupData> batchGroupDataList = new ArrayList<>();
        for (BatchGroup batchGroup: batchGroups){
            List<SuccessPaymentDetail> successPaymentData = successPaymentService.getSuccessPaymentDetails(username, batchGroup);
            long totalBaseAmount = successPaymentData.stream().mapToLong(x -> x.getBaseAmount()).sum();
            BatchGroupData batchGroupData = ObjectMapper.map(batchGroup, BatchGroupData.class);
            batchGroupData.setSuccessPaymentList(successPaymentData);
            batchGroupData.setTotalAmount(totalBaseAmount);
            batchGroupData.setTrxCount(successPaymentData.size());
            batchGroupDataList.add(batchGroupData);
        }
        successPaymentResult.setBatchGroupList(batchGroupDataList);
        return successPaymentResult;
    }

    @Override
    public PaymentResult successPaymentSummary() {
        List<SuccessPaymentSummary> summaryList = new ArrayList<>();
        List<BatchGroup> batchGroups = successPaymentService.getBatchGroup(getUsername());
        for (BatchGroup batchGroup: batchGroups){
            List<SuccessPaymentDetail> successPaymentData = successPaymentService.getSuccessPaymentDetails(getUsername(), batchGroup);
            int batchGroupId = batchGroup.getId();
            long totalBaseAmount = successPaymentData.stream().mapToLong(x -> x.getBaseAmount()).sum();
            SuccessPaymentSummary successPaymentSummary = new SuccessPaymentSummary();
            successPaymentSummary.setBatchGroupId(batchGroupId);
            successPaymentSummary.setName(batchGroup.getName().toUpperCase());
            successPaymentSummary.setTotalAmount(totalBaseAmount);
            successPaymentSummary.setTrxCount(successPaymentData.size());
            successPaymentSummary.setDetailUrl("/trx/success-payment-detail/"+batchGroupId);
            summaryList.add(successPaymentSummary);
        }
        return new PaymentResult().setSummary(summaryList);
    }

    @Override
    public PaymentResult successPaymentDetail(int batchGroupId) {
        BatchGroup batchGroup = getBatchGroup(batchGroupId);
        List<SuccessPaymentDetail> successPaymentDetails = successPaymentService.getSuccessPaymentDetails(getUsername(), batchGroup);
        return new PaymentResult().setDetail(successPaymentDetails);
    }

    @Override
    public void generateFileUpload() {
        tapCashService.generateSettlementData(100);
        eMoneyService.generateSettlementData();
    }

    @Override
    public void uploadSettlementFile() {
        settlementFileUploadService.generateFile();
        settlementFileUploadService.uploadFile();
    }

    @Override
    public SettlementFileUploadResult checkFileUpload() {
        settlementFileUploadService.createSettlementData();
        return new SettlementFileUploadResult().setFileUploads(settlementFileUploadService.checkResponseFile());
    }


    @Override
    public SettlementSummaryResult doSettlement(Integer batchGroupId) {
        SettlementSummaryResult result = null;
        String username = getUsername();
        List<SettlementSessionData> settlementSessionDataList = new ArrayList<>();
        List<BatchGroupData> batchGroupDataList = new ArrayList<>();
        int sessNum = deviceUserDetailService.getSessionNumber(username);
        List<BatchGroup> batchGroups = settlementDataViewService.getBatchGroup(username);
        if (batchGroups.size() == 0)
            throw new ApplicationException(ProcessMessageEnum.NOT_FOUND, "settlement data");
        for (BatchGroup batchGroup : batchGroups) {
            TrxChannel trxChannel = trxChannelService.getTrxChannel(batchGroup.getId());
            boolean isFtp = trxChannel.getTrxChannelMode().equalsIgnoreCase("FTP");
            CardEnum card = CardEnum.getCardEnum(batchGroupId);
            switch (card) {
                case TAPCASH:
                    if (!isFtp){
                        List<SettlementMessage> messages = tapCashService.generateSettlementMessages(sessNum);
                    } else {
                        tapCashService.generateSettlementData(8);
                    }
                    break;
                case EMONEY:
                    eMoneyService.generateSettlementData();
                    break;
                case BRIZZI:
                case FLAZZ:
                default:
                    break;
            }
        }

        return result;
    }

    @Override
    public void updateBalanceInquiry(CardEnum card, ReqUpdateBalance req) {
        InquiryUpdateBalanceResult result = null;
        String username = getUsername();
        switch (card) {
            case TAPCASH:
                result = tapCashService.updateBalanceInquiry(req);
                break;
            case EMONEY:
            case BRIZZI:
            case FLAZZ:
            default:
                break;
        }
    }
}
