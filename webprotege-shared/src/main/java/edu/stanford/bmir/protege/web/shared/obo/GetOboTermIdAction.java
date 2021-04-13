package edu.stanford.bmir.protege.web.shared.obo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetOboTermId")
public abstract class GetOboTermIdAction implements ProjectAction<GetOboTermIdResult> {

    @JsonCreator
    public static GetOboTermIdAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                            @JsonProperty("term") @Nonnull OWLEntity term) {
        return new AutoValue_GetOboTermIdAction(projectId, term);
    }

    @Nonnull
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract OWLEntity getTerm();
}
