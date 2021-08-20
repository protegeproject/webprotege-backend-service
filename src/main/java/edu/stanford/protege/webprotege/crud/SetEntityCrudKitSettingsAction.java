package edu.stanford.protege.webprotege.crud;

import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class SetEntityCrudKitSettingsAction implements ProjectAction<SetEntityCrudKitSettingsResult> {

    public static final String CHANNEL = "entities.SetEntityCrudKitSettings";

    private ProjectId projectId;

    private EntityCrudKitSettings<? extends EntityCrudKitSuffixSettings> fromSettings;

    private EntityCrudKitSettings<? extends EntityCrudKitSuffixSettings> toSettings;

    private IRIPrefixUpdateStrategy prefixUpdateStrategy;

    public SetEntityCrudKitSettingsAction(ProjectId projectId, EntityCrudKitSettings<?> fromSettings, EntityCrudKitSettings<?> toSettings, IRIPrefixUpdateStrategy prefixUpdateStrategy) {
        this.projectId = projectId;
        this.toSettings = toSettings;
        this.fromSettings = fromSettings;
        this.prefixUpdateStrategy = prefixUpdateStrategy;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public IRIPrefixUpdateStrategy getPrefixUpdateStrategy() {
        return prefixUpdateStrategy;
    }

    public EntityCrudKitSettings<? extends EntityCrudKitSuffixSettings> getToSettings() {
        return toSettings;
    }

    public EntityCrudKitSettings<? extends EntityCrudKitSuffixSettings> getFromSettings() {
        return fromSettings;
    }
}
