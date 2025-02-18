package edu.stanford.protege.webprotege.hierarchy.ordering;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.dispatch.Result;


@JsonTypeName(ProcessUploadedSiblingsOrderingAction.CHANNEL)
public record ProcessUploadedSiblingsOrderingResponse() implements Result {

    public static ProcessUploadedSiblingsOrderingResponse create() {
        return new ProcessUploadedSiblingsOrderingResponse();
    }
}
