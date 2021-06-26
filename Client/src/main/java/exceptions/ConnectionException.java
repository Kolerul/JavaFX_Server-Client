package exceptions;

public class ConnectionException extends  RuntimeException{
    private String message;
    public ConnectionException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
