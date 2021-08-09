package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
@AutoValue

@JsonTypeName("CreateNamedIndividuals")
public abstract class CreateNamedIndividualsAction implements AbstractCreateEntitiesAction<CreateNamedIndividualsResult, OWLNamedIndividual> {

    /**
     * Constructs a CreateNamedIndividualsAction.
     * @param projectId The project identifier which identifies the project to create the individuals in.
     * @param types A type for the individuals.  Not {@code null}.
     * @param sourceText The input text that the individuals will be created from.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    @JsonCreator
    public static CreateNamedIndividualsAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                        @JsonProperty("sourceText") @Nonnull String sourceText,
                                        @JsonProperty("langTag") @Nonnull String langTag,
                                        @JsonProperty("types") @Nonnull ImmutableSet<OWLClass> types) {
        return new AutoValue_CreateNamedIndividualsAction(projectId, sourceText, langTag, types);
    }

    /**
     * Gets the type for the individuals.
     * @return The type. Not {@code null}.
     */
    public abstract ImmutableSet<OWLClass> getTypes();

}
