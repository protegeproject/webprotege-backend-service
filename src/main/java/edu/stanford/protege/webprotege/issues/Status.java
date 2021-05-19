package edu.stanford.protege.webprotege.issues;



/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Jul 16
 */
public enum Status {

    OPEN,

    CLOSED;

    public boolean isOpen() {
        return this == OPEN;
    }

    public boolean isClosed() {
        return this == CLOSED;
    }
}
