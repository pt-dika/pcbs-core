package dk.apps.pcps.main.module.tapcash.model.enums;

public enum MTIActionEnum {
    INITIALIZATION("0800", "900000"),
    PARA_BLACK("0800","980000"),
    PARAMETER("0800","980000"),
    BLACKLIST("0800","980000"),
    LOGON("0800","910000"),
    FILE_UPLOAD("0300","980000"),
    LOGOFF("0500","980000"),
    UN_MARRY_SAM("0200","600000"),
    CARD_ACTIVATION("0400","780000"),
    CARD_ACTIVATION_REVERSAL("0200","780000"),
    CARD_UPDATE("0200","860000"),
    CARD_UPDATE_REVERSAL("0400","860000"),
    PENDING_TOPUP_SAVINNGS("0200","841000"),
    PENDING_TOPUP_CHECKING("0200","842000"),
    PENDING_TOPUP_CREDIT_CARD("0200","843000"),
    PENDING_TOPUP_CASH("0200","840000"),
    TOPUP_SAVINNGS("0200","801000"),
    TOPUP_CHECKING("0200","802000"),
    TOPUP_CREDIT_CARD("0200","803000"),
    TOPUP_CASH("0200","800000"),
    TOPUP_REVERSAL("0400","860000");

    public String mti;
    public String processingCode;

    MTIActionEnum(String mti, String processingCode) {
        this.mti = mti;
        this.processingCode = processingCode;
    }
}
