package dk.apps.pcps.main.handler;

import dk.apps.pcps.main.model.enums.ProcessMessageEnum;

public class ApplicationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String[] strings;

    public ApplicationException(ProcessMessageEnum msg) {
        super(msg.name());
    }

    public ApplicationException(ProcessMessageEnum msg, String... strings) {
        super(msg.name());
        this.strings = strings;
    }

    public String getMessages() {
        if (strings == null)
            return "";
        return String.join(" ", strings);
    }
}
