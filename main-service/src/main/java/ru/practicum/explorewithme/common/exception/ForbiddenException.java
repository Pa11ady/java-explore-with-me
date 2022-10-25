package ru.practicum.explorewithme.common.exception;

//@ResponseStatus(HttpStatus.NOT_FOUND)
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}