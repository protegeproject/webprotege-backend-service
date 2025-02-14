package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.frame.translator.Class2ClassFrameTranslatorFactory;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-02
 */
public class ClassFrameProviderImpl implements ClassFrameProvider {

    @Nonnull
    private final Class2ClassFrameTranslatorFactory translatorFactory;

    @Inject
    public ClassFrameProviderImpl(@Nonnull Class2ClassFrameTranslatorFactory classFrameTranslatorFactory) {
        this.translatorFactory = classFrameTranslatorFactory;
    }

    @Nonnull
    @Override
    public PlainClassFrame getFrame(@Nonnull OWLClass subject,
                                    @Nonnull ClassFrameTranslationOptions options) {
        var translator = translatorFactory.create(options);
        return translator.getFrame(subject);
    }
}
