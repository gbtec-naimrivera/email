package Service.Exceptions;

public class InvalidEmailStateException extends RuntimeException {
    public InvalidEmailStateException(String message) {
        super(message);
    }
}