package dk.apps.pcps.main.handler;

public class UnauthorizedException extends Exception {
    private static final long serialVersionUID = 1L;

    public UnauthorizedException(String msg) {
        super(msg);
    }
}
