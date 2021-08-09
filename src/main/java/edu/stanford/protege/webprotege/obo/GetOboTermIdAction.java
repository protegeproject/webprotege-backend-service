package edu.stanford.protege.webprotege.obo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
@AutoValue

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
