package ru.practicum.explorewithme.common.exception;

public class StateIsNotSupportException extends RuntimeException {
    public StateIsNotSupportException(String message) {
        super(message);
    }
}