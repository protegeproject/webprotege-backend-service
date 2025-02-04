package edu.stanford.protege.webprotege.renderer;

import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.shortform.DictionaryManager;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.util.ShortFormProvider;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Apr 2018
 */
@ProjectSingleton
public class ShortFormAdapter implements ShortFormProvider {

    @Nonnull
    private final DictionaryManager dictionaryManager;

    @Inject
    public ShortFormAdapter(@Nonnull DictionaryManager dictionaryManager) {
        this.dictionaryManager = checkNotNull(dictionaryManager);
    }

    @Nonnull
    @Override
    public String getShortForm(@Nonnull OWLEntity entity) {
        return dictionaryManager.getShortForm(entity);
    }

    @Override
    public void dispose() {

    }
}
