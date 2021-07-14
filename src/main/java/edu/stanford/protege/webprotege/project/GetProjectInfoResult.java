package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.lang.DictionaryLanguageUsage;
import edu.stanford.protege.webprotege.projectsettings.ProjectSettings;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Aug 2018
 */
@AutoValue

@JsonTypeName("GetProjectInfo")
public abstract class GetProjectInfoResult implements Result {

    @JsonCreator
    public static GetProjectInfoResult create(@JsonProperty("projectSettings") @Nonnull ProjectSettings projectSettings,
                                              @JsonProperty("projectLanguages") @Nonnull ImmutableList<DictionaryLanguageUsage> languageUsage) {
        return new AutoValue_GetProjectInfoResult(projectSettings,
                                                  languageUsage);
    }

    @Nonnull
    public abstract ProjectSettings getProjectSettings();

    @Nonnull
    public abstract ImmutableList<DictionaryLanguageUsage> getProjectLanguages();
}
