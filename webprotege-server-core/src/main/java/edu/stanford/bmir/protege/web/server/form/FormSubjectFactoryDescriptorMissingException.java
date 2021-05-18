package edu.stanford.bmir.protege.web.server.form;



/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
public class FormSubjectFactoryDescriptorMissingException extends RuntimeException {

    public FormSubjectFactoryDescriptorMissingException() {
        super("Subject factory descriptor is missing.  Improperly configured form descriptor.");
    }
}
