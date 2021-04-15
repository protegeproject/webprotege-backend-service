package edu.stanford.bmir.protege.web.shared.project;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/16
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetProjectDetails")
public abstract class GetProjectDetailsResult implements Result {

    public static GetProjectDetailsResult get(@Nonnull ProjectDetails projectDetails) {
        return new AutoValue_GetProjectDetailsResult(projectDetails);
    }

    @Nonnull
    public abstract ProjectDetails getProjectDetails();
}
