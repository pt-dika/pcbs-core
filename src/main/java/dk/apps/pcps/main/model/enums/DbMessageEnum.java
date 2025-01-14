package dk.apps.pcps.main.model.enums;

public enum DbMessageEnum {
    SUCCESS("0200","successfully"),
    NOT_FOUND("0201","data not found"),
    ALREADY_EXISTS("0202","data already exists"),
    FAILED_CREATED("0203","data failed created"),
    FAILED_UPDATED("0204","data failed updated");

    public String code;
    public String message;

    DbMessageEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
