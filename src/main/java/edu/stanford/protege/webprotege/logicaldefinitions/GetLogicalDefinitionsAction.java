package edu.stanford.protege.webprotege.logicaldefinitions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;

@JsonTypeName("icatx.logicaldefinitions.GetLogicalDefinitions")
public record GetLogicalDefinitionsAction(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                          @JsonProperty("subject") @Nonnull IRI subject
                                          ) {
}
