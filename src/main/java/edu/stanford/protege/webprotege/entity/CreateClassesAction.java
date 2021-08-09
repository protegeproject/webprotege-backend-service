package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
@AutoValue

@JsonTypeName("CreateClasses")
public abstract class CreateClassesAction implements CreateEntitiesInHierarchyAction<CreateClassesResult, OWLClass> {


    @JsonCreator
    public static CreateClassesAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                               @JsonProperty("sourceText") @Nonnull String sourceText,
                               @JsonProperty("langTag") @Nonnull String langTag,
                               @JsonProperty("parents") @Nonnull ImmutableSet<OWLClass> parents) {
        return new AutoValue_CreateClassesAction(projectId, sourceText, langTag, parents);
    }
}
