package exceptions;
/**
 * Illegal count of arguments exception
 */
public class IllegalCountOfArgumentsException extends RuntimeException{
    /**
     * @return error message
     */
    @Override
    public String getMessage() {
        return "Неправильное число аргкментов";
    }
}
