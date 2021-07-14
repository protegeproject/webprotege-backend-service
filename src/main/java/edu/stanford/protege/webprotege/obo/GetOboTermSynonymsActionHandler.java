package edu.stanford.protege.webprotege.obo;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Jun 2017
 */
public class GetOboTermSynonymsActionHandler extends AbstractProjectActionHandler<GetOboTermSynonymsAction, GetOboTermSynonymsResult> {

    @Nonnull
    private final TermSynonymsManager synonymsManager;

    @Inject
    public GetOboTermSynonymsActionHandler(@Nonnull AccessManager accessManager,
                                           @Nonnull TermSynonymsManager synonymsManager) {
        super(accessManager);
        this.synonymsManager = synonymsManager;
    }

    @Nonnull
    @Override
    public Class<GetOboTermSynonymsAction> getActionClass() {
        return GetOboTermSynonymsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetOboTermSynonymsAction action) {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetOboTermSynonymsResult execute(@Nonnull GetOboTermSynonymsAction action, @Nonnull ExecutionContext executionContext) {
        Collection<OBOTermSynonym> synonyms = synonymsManager.getSynonyms(action.getEntity());
        return GetOboTermSynonymsResult.create(new ArrayList<>(synonyms));
    }
}
