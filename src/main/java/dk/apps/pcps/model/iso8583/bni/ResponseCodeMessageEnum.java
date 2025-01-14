package dk.apps.pcps.model.iso8583.bni;

public enum ResponseCodeMessageEnum {

    SUCCESS("00","Success"),
    INVALID_TERMINAL("03","Invalid Terminal"),
    INVALID_TRANSACTION("12","Invalid Transaction"),
    INVALID_AMOUNT("13","Invalid Amount"),
    INVALID_CARD("31","Invalid Card"),
    CARD_BLACKLISTED("32","Card Blacklisted"),
    CARD_EXPIRED("33","Card Expired"),
    INVALID_CARD_STATUS("34","Invalid Card Status"),
    DECLINED("51","Declined (General Error or Others)"),
    INCORRECT_PIN("55","Incorrect Pin"),
    LIMIT_EXCEEDED("61","Limit Exceeded"),
    MAC_ERROR("98","MAC Error"),
    DUPLICATE_REVERSAL("94","Duplicate Reversal"),
    NO_DATA("14","No Data to Update"),
    PENDING_TOPUP("99","Pending Topup Available"),
    UNKNOWN("","Unknown Error");

    public String code;
    public String message;

    ResponseCodeMessageEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResponseCodeMessageEnum getByCode(String code) {
        for(ResponseCodeMessageEnum e : values()) {
            if(e.code.equals(code)) return e;
        }
        return UNKNOWN;
    }
}
