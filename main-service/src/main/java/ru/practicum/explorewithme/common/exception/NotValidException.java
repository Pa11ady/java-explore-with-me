package ru.practicum.explorewithme.common.exception;

//@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotValidException extends RuntimeException {
    public NotValidException(String message) {
        super(message);
    }
}