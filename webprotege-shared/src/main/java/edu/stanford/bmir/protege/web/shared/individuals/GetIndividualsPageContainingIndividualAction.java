package edu.stanford.bmir.protege.web.shared.individuals;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Sep 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetIndividualsPageContainingIndividual")
public abstract class GetIndividualsPageContainingIndividualAction implements ProjectAction<GetIndividualsPageContainingIndividualResult> {


    @JsonCreator
    public static GetIndividualsPageContainingIndividualAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                                      @JsonProperty("individual") @Nonnull OWLNamedIndividual individual,
                                                                      @JsonProperty("preferredType") @Nonnull Optional<OWLClass> preferredType,
                                                                      @JsonProperty("preferredMode") @Nullable InstanceRetrievalMode preferredMode) {
        return new AutoValue_GetIndividualsPageContainingIndividualAction(projectId, individual, preferredType, preferredMode);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract OWLNamedIndividual getIndividual();

    @Nonnull
    public abstract Optional<OWLClass> getPreferredType();

    @Nonnull
    public abstract InstanceRetrievalMode getPreferredMode();
}
