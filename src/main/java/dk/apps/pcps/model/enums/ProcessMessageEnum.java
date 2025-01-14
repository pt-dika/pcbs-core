package dk.apps.pcps.model.enums;

public enum ProcessMessageEnum {
    SUCCESS("0200","successfully"),
    USER_NOT_LOGIN("0201","user not login, please login for try this transaction"),
    NOT_FOUND("0204", "not found"),
    ALREADY_PAID("0205", "already paid"),
    CARD_NOT_FOUND("0211","card not found"),
    CHANNEL_NOT_DEFINED("0212","channel not defined"),
    BATCH_GROUP_MDR_NOT_CONFIGURED("0213","batch group mdr not configured"),
    MERCHANT_NOT_CONFIGURE_BATCH_GROUP("0214","merchant not configure batch group"),
    MERCHANT_MDR_NOT_CONFIGURED("0215","merchant mdr not configured"),
    MERCHANT_NOT_DEFINED("0216","merchant not defined"),
    USER_NOT_FOUND("0217","user not found"),
    USER_NOT_CONFIGURE_BATCH_GROUP("0218","user not configure batch group"),
    FAILED_UPDATE_CARD("0220","failed update card"),
    FAILED_UPDATED("0221","failed updated"),
    FAILED_SETTLEMENT("0222","failed settlement"),
    FAILED_LOGON("0223","failed logon"),
    FAILED_LOGOFF("0224","failed logoff"),
    SAM_NOT_PAIR("0230", "sam card not pair with device"),
    OUT_OF_RANGE("0250", "range max of 7 last days");


    public String code;
    public String message;

    ProcessMessageEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
