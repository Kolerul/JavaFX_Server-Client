package exceptions;

public class InfiniteRecursionException extends RuntimeException{
    @Override
    public String getMessage() {
        return "Была встречена бесконечная рекурсия";
    }
}
