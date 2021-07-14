package edu.stanford.protege.webprotege.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-15
 */
@AutoValue

@JsonTypeName("DeleteForm")
public abstract class DeleteFormAction implements ProjectAction<DeleteFormResult> {

    @JsonCreator
    public static DeleteFormAction get(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                            @JsonProperty("formId") @Nonnull FormId formId) {
        return new AutoValue_DeleteFormAction(projectId, formId);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract FormId getFormId();
}
