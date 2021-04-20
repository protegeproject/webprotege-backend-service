package edu.stanford.bmir.protege.web.shared.ontology;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/08/2013
 */
@AutoValue

@JsonTypeName("SetOntologyAnnotations")
public abstract class SetOntologyAnnotationsAction implements ProjectAction<SetOntologyAnnotationsResult> {


    @JsonCreator
    public static SetOntologyAnnotationsAction create(@JsonProperty("projectId") ProjectId projectId,
                                                      @JsonProperty("ontologyId") OWLOntologyID ontologyID,
                                                      @JsonProperty("fromAnnotations") Set<PropertyAnnotationValue> fromAnnotations,
                                                      @JsonProperty("toAnnotations") Set<PropertyAnnotationValue> toAnnotations) {
        return new AutoValue_SetOntologyAnnotationsAction(projectId, ontologyID, fromAnnotations, toAnnotations);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    public abstract OWLOntologyID getOntologyId();

    public abstract Set<PropertyAnnotationValue> getFromAnnotations();

    public abstract Set<PropertyAnnotationValue> getToAnnotations();
}
