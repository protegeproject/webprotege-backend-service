package edu.stanford.protege.webprotege.hierarchy;

public class HierarchyCycleException extends RuntimeException {

    public HierarchyCycleException() {
        super("Changed class hierarchy contains a cycle!");
    }

    public HierarchyCycleException(String message) {
        super(message);
    }
}
