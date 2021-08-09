package edu.stanford.protege.webprotege.lang;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.MoreObjects;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
@JsonTypeName("GetProjectLangTags")
public class GetProjectLangTagsAction implements ProjectAction<GetProjectLangTagsResult> {

    private ProjectId projectId;

    private GetProjectLangTagsAction(@Nonnull ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }


    private GetProjectLangTagsAction() {
    }

    public static GetProjectLangTagsAction create(@Nonnull ProjectId projectId) {
        return new GetProjectLangTagsAction(projectId);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public int hashCode() {
        return projectId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof GetProjectLangTagsAction)) {
            return false;
        }
        GetProjectLangTagsAction other = (GetProjectLangTagsAction) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("GetProjectLangTagsAction")
                .addValue(projectId)
                .toString();
    }
}
