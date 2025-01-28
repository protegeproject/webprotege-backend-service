package edu.stanford.protege.webprotege.shortform;

import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2018
 */
@ProjectSingleton
public class LocalNameShortFormCache {

    @Nonnull
    private final ShortFormCache shortFormCache;

    @Nonnull
    private final LocalNameExtractor localNameExtractor;

    @Inject
    public LocalNameShortFormCache(@Nonnull ShortFormCache shortFormCache,
                                   @Nonnull LocalNameExtractor localNameExtractor) {
        this.shortFormCache = checkNotNull(shortFormCache);
        this.localNameExtractor = checkNotNull(localNameExtractor);
    }

    public String getShortForm(OWLEntity entity) {
        String localNameShortForm = shortFormCache.getShortFormOrElse(entity, null);
        if(localNameShortForm == null) {
            String localName = localNameExtractor.getLocalName(entity.getIRI());
            if(localName.isEmpty()) {
                return entity.getIRI().toString();
            }
            else {
                shortFormCache.put(entity, localName);
                return localName;
            }
        }
        else {
            return localNameShortForm;
        }
    }
}
