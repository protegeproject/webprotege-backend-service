package edu.stanford.protege.webprotege.lang;

import edu.stanford.protege.webprotege.common.DictionaryLanguage;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.project.ProjectDetailsRepository;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Apr 2018
 */
@ProjectSingleton
public class LanguageManager {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ActiveLanguagesManager activeLanguagesManager;

    @Nonnull
    private final ProjectDetailsRepository projectDetailsRepository;

    @Inject
    public LanguageManager(@Nonnull ProjectId projectId,
                           @Nonnull ActiveLanguagesManager extractor,
                           @Nonnull ProjectDetailsRepository projectDetailsRepository) {
        this.projectId = projectId;
        this.activeLanguagesManager = checkNotNull(extractor);
        this.projectDetailsRepository = projectDetailsRepository;
    }

    public synchronized List<DictionaryLanguage> getLanguages() {
        var defaultDisplayLanguages = projectDetailsRepository.getDisplayNameLanguages(projectId);
        if (defaultDisplayLanguages.isEmpty()) {
            return activeLanguagesManager.getLanguagesRankedByUsage();
        }
        else {
            return defaultDisplayLanguages;
        }
    }

    public synchronized List<DictionaryLanguage> getActiveLanguages() {
        return activeLanguagesManager.getLanguagesRankedByUsage();
    }
}
