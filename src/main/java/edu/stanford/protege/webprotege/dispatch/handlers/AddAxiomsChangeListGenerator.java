package edu.stanford.protege.webprotege.dispatch.handlers;

import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.dispatch.actions.AddAxiomsAction;
import edu.stanford.protege.webprotege.owlapi.RenameMap;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-02-25
 */
public class AddAxiomsChangeListGenerator implements ChangeListGenerator<Integer> {

    private final DefaultOntologyIdManager defaultOntologyIdManager;

    private final AddAxiomsAction action;

    @Inject
    public AddAxiomsChangeListGenerator(DefaultOntologyIdManager defaultOntologyIdManager,
                                        AddAxiomsAction action) {
        this.defaultOntologyIdManager = defaultOntologyIdManager;
        this.action = action;
    }

    @Override
    public OntologyChangeList<Integer> generateChanges(ChangeGenerationContext context) {
        var builder = OntologyChangeList.<Integer>builder();
        var ontId = defaultOntologyIdManager.getDefaultOntologyId();
        action.axiomsSource().getAxioms().forEach(ax -> builder.add(AddAxiomChange.of(ontId, ax)));
        return builder.build(0);
    }

    @Override
    public Integer getRenamedResult(Integer result, RenameMap renameMap) {
        return result;
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<Integer> result) {
        return action.commitMessage();
    }

    @Override
    public ChangeRequestId getChangeRequestId() {
        return action.changeRequestId();
    }
}
