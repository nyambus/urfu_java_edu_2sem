package ru.education.lab4.exception;

public class ValidationFailedException extends RuntimeException {
    public ValidationFailedException(String message) { super(message); }
}
