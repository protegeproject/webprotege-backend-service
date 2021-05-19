package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
@AutoValue

@JsonTypeName("CreateAnnotationProperties")
public abstract class CreateAnnotationPropertiesAction implements CreateEntitiesInHierarchyAction<CreateAnnotationPropertiesResult, OWLAnnotationProperty> {

    @JsonCreator
    public static CreateAnnotationPropertiesAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                            @JsonProperty("sourceText") @Nonnull String sourceText,
                                            @JsonProperty("langTag") @Nonnull String langTag,
                                            @JsonProperty("parents") @Nonnull ImmutableSet<OWLAnnotationProperty> parents) {
        return new AutoValue_CreateAnnotationPropertiesAction(projectId, sourceText, langTag, parents);
    }
}
