package edu.stanford.bmir.protege.web.server.frame;



/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/07/15
 */
public class SetManchesterSyntaxFrameException extends RuntimeException {

    private ManchesterSyntaxFrameParseError error;

    private SetManchesterSyntaxFrameException() {
    }

    public SetManchesterSyntaxFrameException(ManchesterSyntaxFrameParseError error) {
        super(error.getMessage());
        this.error = error;
    }

    public ManchesterSyntaxFrameParseError getError() {
        return error;
    }
}
