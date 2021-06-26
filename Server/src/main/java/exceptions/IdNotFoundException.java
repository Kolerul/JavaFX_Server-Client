package exceptions;

/**
 * Id not found exception
 */
public class IdNotFoundException extends RuntimeException {
    /**
     * @return error message
     */
    @Override
    public String getMessage() {
        return "Вашего элемента с таким id найти не удалось.";
    }
}
