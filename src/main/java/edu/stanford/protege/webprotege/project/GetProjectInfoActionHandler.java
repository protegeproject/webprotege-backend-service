package edu.stanford.protege.webprotege.project;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.lang.ActiveLanguagesManager;
import edu.stanford.protege.webprotege.lang.DictionaryLanguageUsage;
import edu.stanford.protege.webprotege.projectsettings.ProjectSettings;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Aug 2018
 */
public class GetProjectInfoActionHandler extends AbstractProjectActionHandler<GetProjectInfoAction, GetProjectInfoResult> {

    @Nonnull
    private final ProjectDetailsManager projectDetailsManager;

    @Nonnull
    private final ActiveLanguagesManager activeLanguagesManager;

    @Inject
    public GetProjectInfoActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull ProjectDetailsManager projectDetailsManager,
                                       @Nonnull ActiveLanguagesManager activeLanguagesManager) {
        super(accessManager);
        this.projectDetailsManager = checkNotNull(projectDetailsManager);
        this.activeLanguagesManager = checkNotNull(activeLanguagesManager);
    }

    @Nonnull
    @Override
    public Class<GetProjectInfoAction> getActionClass() {
        return GetProjectInfoAction.class;
    }

    @Nonnull
    @Override
    public GetProjectInfoResult execute(@Nonnull GetProjectInfoAction action, @Nonnull ExecutionContext executionContext) {
        ProjectId projectId = action.projectId();
        ProjectSettings projectSettings = projectDetailsManager.getProjectSettings(projectId);
        ImmutableList<DictionaryLanguageUsage> languageUsage = activeLanguagesManager.getLanguageUsage();
        return GetProjectInfoResult.create(projectSettings, languageUsage);
    }
}
