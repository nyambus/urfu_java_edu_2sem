package ru.education.lab4.exception;

public class UnsupportedCodeException extends RuntimeException {
    public UnsupportedCodeException() { super("Идентификатор uid=123 не поддерживается"); }
}
