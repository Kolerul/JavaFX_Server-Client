package exceptions;
/**
 * Incorrect input data exception
 */
public class IncorrectInputDataException extends RuntimeException {
    /**
     * @return error message
     */
    @Override
    public String getMessage() {
        return "Неправильные входные данные";
    }
}
