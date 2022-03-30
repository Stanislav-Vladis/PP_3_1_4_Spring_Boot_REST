package academy.kata.platform.spring.spring_boot_rest.exception.api;

public class NoSuchUserException extends RuntimeException {

    public NoSuchUserException(String message) {
        super(message);
    }

}
