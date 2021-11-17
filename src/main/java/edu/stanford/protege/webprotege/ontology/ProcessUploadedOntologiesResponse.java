package edu.stanford.protege.webprotege.ontology;

import edu.stanford.protege.webprotege.common.BlobLocation;
import edu.stanford.protege.webprotege.common.Response;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-11-16
 */
public record ProcessUploadedOntologiesResponse(List<BlobLocation> ontologies) implements Response {

}
