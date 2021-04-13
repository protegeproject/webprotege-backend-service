package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
@AutoValue
@GwtCompatible(serializable = true)
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
