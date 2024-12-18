package edu.stanford.protege.webprotege.icd.exceptions;

public class FileActionException extends RuntimeException {
    public FileActionException(String message, Throwable exception) {
        super(message, exception);
    }
}
