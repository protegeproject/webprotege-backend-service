package edu.stanford.protege.webprotege.projectsettings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.crud.EntityCrudKitSettings;
import edu.stanford.protege.webprotege.project.PrefixDeclaration;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.project.WithProjectId;
import edu.stanford.protege.webprotege.search.ProjectSearchSettings;
import edu.stanford.protege.webprotege.sharing.ProjectSharingSettings;
import edu.stanford.protege.webprotege.tag.Tag;

import javax.annotation.Nonnull;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-24
 */
@AutoValue
public abstract class AllProjectSettings implements WithProjectId<AllProjectSettings> {

    public static final String PROJECT_SETTINGS = "projectSettings";

    public static final String ENTITY_CREATION_SETTINGS = "entityCreationSettings";

    public static final String PREFIX_DECLARATIONS = "prefixDeclarations";

    public static final String PROJECT_TAGS = "projectTags";

    public static final String SHARING_SETTINGS = "sharingSettings";

    public static final String SEARCH_SETTINGS = "searchSettings";

    @JsonCreator
    @Nonnull
    public static AllProjectSettings get(@JsonProperty(PROJECT_SETTINGS) @Nonnull ProjectSettings projectSettings,
                                         @JsonProperty(ENTITY_CREATION_SETTINGS) @Nonnull EntityCrudKitSettings entityCrudKitSettings,
                                         @JsonProperty(PREFIX_DECLARATIONS) @Nonnull ImmutableList<PrefixDeclaration> prefixDeclarations,
                                         @JsonProperty(PROJECT_TAGS) @Nonnull ImmutableList<Tag> tags,
                                         @JsonProperty(SHARING_SETTINGS) @Nonnull ProjectSharingSettings projectSharingSettings,
                                         @JsonProperty(SEARCH_SETTINGS) @Nonnull ProjectSearchSettings projectSearchSettings) {
        return new AutoValue_AllProjectSettings(projectSettings, entityCrudKitSettings, prefixDeclarations,
                                                tags, projectSharingSettings, projectSearchSettings);
    }

    @JsonProperty(PROJECT_SETTINGS)
    @Nonnull
    public abstract ProjectSettings getProjectSettings();

    @JsonProperty(ENTITY_CREATION_SETTINGS)
    @Nonnull
    public abstract EntityCrudKitSettings getEntityCreationSettings();

    @JsonProperty(PREFIX_DECLARATIONS)
    @Nonnull
    public abstract ImmutableList<PrefixDeclaration> getPrefixDeclarations();

    @JsonProperty(PROJECT_TAGS)
    @Nonnull
    public abstract ImmutableList<Tag> getProjectTags();

    @JsonProperty(SHARING_SETTINGS)
    @Nonnull
    public abstract ProjectSharingSettings getSharingSettings();

    @JsonProperty(SEARCH_SETTINGS)
    @Nonnull
    public abstract ProjectSearchSettings getSearchSettings();

    @Override
    public AllProjectSettings withProjectId(@Nonnull ProjectId projectId) {
        return AllProjectSettings.get(
                getProjectSettings().withProjectId(projectId),
                getEntityCreationSettings(),
                getPrefixDeclarations(),
                getProjectTags().stream().map(t -> t.withProjectId(projectId)).collect(toImmutableList()),
                getSharingSettings(),
                getSearchSettings()
        );
    }
}
