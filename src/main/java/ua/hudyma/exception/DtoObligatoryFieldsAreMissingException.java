package ua.hudyma.exception;

public class DtoObligatoryFieldsAreMissingException extends RuntimeException{
    public DtoObligatoryFieldsAreMissingException(String message) {
        super(message);
    }
}
