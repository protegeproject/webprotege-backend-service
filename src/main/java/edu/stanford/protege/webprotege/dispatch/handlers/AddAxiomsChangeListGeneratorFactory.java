package edu.stanford.protege.webprotege.dispatch.handlers;

import edu.stanford.protege.webprotege.dispatch.actions.AddAxiomsAction;
import edu.stanford.protege.webprotege.dispatch.actions.AxiomsSource;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-02-25
 */
public class AddAxiomsChangeListGeneratorFactory {

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    public AddAxiomsChangeListGeneratorFactory(@Nonnull DefaultOntologyIdManager defaultOntologyIdManager) {
        this.defaultOntologyIdManager = checkNotNull(defaultOntologyIdManager);
    }

    public AddAxiomsChangeListGenerator create(AddAxiomsAction action) {
        return new AddAxiomsChangeListGenerator(defaultOntologyIdManager, action);
    }
}
