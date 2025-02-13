package edu.stanford.protege.webprotege.shortform;

import edu.stanford.protege.webprotege.common.DictionaryLanguage;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.Collection;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-13
 */
public class LuceneMultiLingualDictionaryUpdater implements MultilingualDictionaryUpdater {

    @Nonnull
    private final LuceneIndexUpdater indexUpdater;

    @Inject
    public LuceneMultiLingualDictionaryUpdater(@Nonnull LuceneIndexUpdater indexUpdater) {
        this.indexUpdater = indexUpdater;
    }

    @Override
    public void update(@Nonnull Collection<OWLEntity> entities,
                       @Nonnull List<DictionaryLanguage> languages) {
        indexUpdater.updateIndexForEntities(entities);
    }
}
