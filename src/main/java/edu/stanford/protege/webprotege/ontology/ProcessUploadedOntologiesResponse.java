package edu.stanford.protege.webprotege.ontology;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.BlobLocation;
import edu.stanford.protege.webprotege.common.Response;

import java.util.List;
import java.util.Objects;

import static edu.stanford.protege.webprotege.ontology.ProcessUploadedOntologiesRequest.CHANNEL;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-11-16
 */
@JsonTypeName(CHANNEL)
public record ProcessUploadedOntologiesResponse(List<BlobLocation> ontologies) implements Response {

    public ProcessUploadedOntologiesResponse(List<BlobLocation> ontologies) {
        this.ontologies = Objects.requireNonNull(ontologies);
    }
}
