package dk.apps.pcps.main.handler;

public class MessageException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String[] strings;

    public MessageException(String msg) {
        super(msg);
    }

    public MessageException(String msg, String... strings) {
        super(msg);
        this.strings = strings;
    }

    public String getMessages() {
        if (strings == null)
            return "";
        return String.join(" ", strings);
    }
}
