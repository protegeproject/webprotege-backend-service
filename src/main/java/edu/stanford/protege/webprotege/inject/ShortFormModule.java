package edu.stanford.protege.webprotege.inject;

import dagger.Module;
import dagger.Provides;
import edu.stanford.protege.webprotege.lang.ActiveLanguagesManager;
import edu.stanford.protege.webprotege.lang.ActiveLanguagesManagerImpl;
import edu.stanford.protege.webprotege.shortform.*;
import org.semanticweb.owlapi.model.OWLEntityProvider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 */
@Module(includes = LuceneModule.class)
public class ShortFormModule {


    @Provides
    ActiveLanguagesManager provideActiveLanguagesManager(ActiveLanguagesManagerImpl impl) {
        return impl;
    }

    @Provides
    ShortFormCache provideShortFormCache() {
        return ShortFormCache.create();
    }

    @Provides
    BuiltInShortFormDictionary provideBuiltInShortFormDictionary(ShortFormCache cache, OWLEntityProvider provider) {
        BuiltInShortFormDictionary dictionary = new BuiltInShortFormDictionary(cache, provider);
        dictionary.load();
        return dictionary;
    }


}
