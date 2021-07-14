package edu.stanford.protege.webprotege.lang;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
@JsonTypeName("GetProjectLangTags")
public class GetProjectLangTagsResult implements Result {

    private ProjectId projectId;

    private ImmutableSet<LangTag> langTags;

    private GetProjectLangTagsResult(@Nonnull ProjectId projectId, @Nonnull ImmutableSet<LangTag> langTags) {
        this.projectId = checkNotNull(projectId);
        this.langTags = checkNotNull(langTags);
    }


    private GetProjectLangTagsResult() {
    }

    public static GetProjectLangTagsResult create(@Nonnull ProjectId projectId,
                                                  @Nonnull ImmutableSet<LangTag> langTags) {
        return new GetProjectLangTagsResult(projectId, langTags);
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public ImmutableSet<LangTag> getLangTags() {
        return langTags;
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, langTags);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof GetProjectLangTagsResult)) {
            return false;
        }
        GetProjectLangTagsResult other = (GetProjectLangTagsResult) obj;
        return this.projectId.equals(other.projectId) && this.langTags.equals(other.langTags);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("GetProjectLangTagsResult")
                .addValue(projectId)
                .add("langTags", langTags)
                .toString();
    }
}
