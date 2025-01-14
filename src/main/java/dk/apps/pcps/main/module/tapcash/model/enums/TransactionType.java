package dk.apps.pcps.main.module.tapcash.model.enums;

public enum TransactionType {
    PURCHASE("01","Purchase"),
    BLCARD("02","Blacklist Card"),
    CASH_TOPUP("04","Cash Topup"),
    STATEMENT_FEE("05","Statement Fee"),
    CARD_UPDATE("06","Card Update"),
    GRACE_PERIOD_DEBIT("07","Grace Period Debit"),
    UNKNOWN("00","Unknown");

    public String code;
    public String message;

    TransactionType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static TransactionType getCardEnum(String code){
        for(TransactionType e : values()) {
            if(e.code == code) return e;
        }
        return UNKNOWN;
    }
}
