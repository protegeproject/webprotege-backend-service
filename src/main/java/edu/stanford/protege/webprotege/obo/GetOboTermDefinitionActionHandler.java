package edu.stanford.protege.webprotege.obo;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Jun 2017
 */
public class GetOboTermDefinitionActionHandler extends AbstractProjectActionHandler<GetOboTermDefinitionAction, GetOboTermDefinitionResult> {

    @Nonnull
    private final TermDefinitionManager termDefinitionManager;

    @Inject
    public GetOboTermDefinitionActionHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull TermDefinitionManager termDefinitionManager) {
        super(accessManager);
        this.termDefinitionManager = termDefinitionManager;
    }

    @Nonnull
    @Override
    public Class<GetOboTermDefinitionAction> getActionClass() {
        return GetOboTermDefinitionAction.class;
    }

    @Nonnull
    @Override
    public GetOboTermDefinitionResult execute(@Nonnull GetOboTermDefinitionAction action, @Nonnull ExecutionContext executionContext) {
        return new GetOboTermDefinitionResult(termDefinitionManager.getTermDefinition(action.term()));
    }




}
