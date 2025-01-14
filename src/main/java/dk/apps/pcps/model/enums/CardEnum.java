package dk.apps.pcps.model.enums;

public enum  CardEnum {
    TAPCASH(1,"Tapcash"),
    EMONEY(2,"Mandiri E-Money"),
    BRIZZI(3,"BRI Brizzi"),
    FLAZZ(4,"Flazz"),
    JAKCARD(5,"DKI JakCard"),
    UNKNOWN(0,"Unknown");

    public int code;
    public String message;

    CardEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CardEnum getCardEnum(int code){
        for(CardEnum e : values()) {
            if(e.code == code) return e;
        }
        return UNKNOWN;
    }
}
