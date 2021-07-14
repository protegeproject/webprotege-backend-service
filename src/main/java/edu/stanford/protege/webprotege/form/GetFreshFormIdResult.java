package edu.stanford.protege.webprotege.form;

import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.project.HasProjectId;
import edu.stanford.protege.webprotege.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-16
 */
@AutoValue

public abstract class GetFreshFormIdResult implements Result, HasProjectId {

    public static GetFreshFormIdResult get(@Nonnull ProjectId projectId,
                                           @Nonnull FormId formId) {
        return new AutoValue_GetFreshFormIdResult(projectId, formId);
    }

    @Nonnull
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract FormId getFormId();
}
