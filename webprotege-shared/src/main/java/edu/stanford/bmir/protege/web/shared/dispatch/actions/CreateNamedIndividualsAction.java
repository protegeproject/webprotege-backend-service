package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
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
