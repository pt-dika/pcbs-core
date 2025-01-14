package dk.apps.pcps.main.model.enums;

public enum  CardEnum {
    TAPCASH(1, "tapcash","Tapcash"),
    EMONEY(2,"e-money","Mandiri E-Money"),
    BRIZZI(3,"brizzi","BRI Brizzi"),
    FLAZZ(4,"flazz","Flazz"),
    JAKCARD(5,"jakcard","DKI JakCard"),
    UNKNOWN(0,"","Unknown");

    public int code;
    public String name;
    public String message;

    CardEnum(int code, String name, String message) {
        this.code = code;
        this.name = name;
        this.message = message;
    }

    public static CardEnum getCardEnum(int code){
        for(CardEnum e : values()) {
            if(e.code == code) return e;
        }
        return UNKNOWN;
    }
}
