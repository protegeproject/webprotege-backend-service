package edu.stanford.bmir.protege.web.server.ontology;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.frame.PropertyAnnotationValue;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
@AutoValue

@JsonTypeName("GetOntologyAnnotations")
public abstract class GetOntologyAnnotationsResult implements Result {


    @JsonCreator
    public static GetOntologyAnnotationsResult create(@JsonProperty("ontologyId") OWLOntologyID ontologyID,
                                                      @JsonProperty("annotations") ImmutableList<PropertyAnnotationValue> annotations) {
        return new AutoValue_GetOntologyAnnotationsResult(ontologyID, annotations);
    }

    @Nonnull
    public abstract OWLOntologyID getOntologyId();

    @Nonnull
    public abstract ImmutableList<PropertyAnnotationValue> getAnnotations();
}
