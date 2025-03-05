package edu.stanford.protege.webprotege.diff;

public record OrderChange(String initialPreviousElement,
                          String initialNextElement,
                          String newPreviousElement,
                          String newNextElement) {

}
