package exceptions;

public class InvalidInputException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Слишком длинно";
    }
}
