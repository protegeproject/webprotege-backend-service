package edu.stanford.protege.webprotege.lang;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInCapability;
import edu.stanford.protege.webprotege.common.DictionaryLanguage;
import edu.stanford.protege.webprotege.common.LangTag;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jakarta.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
public class GetProjectLangTagsActionHandler extends AbstractProjectActionHandler<GetProjectLangTagsAction, GetProjectLangTagsResult> {

    @Nonnull
    private final LanguageManager languageManager;

    @Inject
    public GetProjectLangTagsActionHandler(@Nonnull AccessManager accessManager,
                                           @Nonnull LanguageManager languageManager) {
        super(accessManager);
        this.languageManager = checkNotNull(languageManager);
    }

    @Nonnull
    @Override
    public Class<GetProjectLangTagsAction> getActionClass() {
        return GetProjectLangTagsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInCapability getRequiredExecutableBuiltInAction(GetProjectLangTagsAction action) {
        return BuiltInCapability.VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetProjectLangTagsResult execute(@Nonnull GetProjectLangTagsAction action,
                                            @Nonnull ExecutionContext executionContext) {
        var langTags = languageManager.getActiveLanguages()
                .stream()
                .map(DictionaryLanguage::getLang)
                .filter(l -> !l.isBlank())
                .map(LangTag::get)
                .collect(toImmutableSet());
        return new GetProjectLangTagsResult(action.projectId(), langTags);

    }
}
