package edu.stanford.protege.webprotege.frame.translator;


import edu.stanford.protege.webprotege.frame.PlainPropertyAnnotationValue;
import edu.stanford.protege.webprotege.frame.PlainPropertyValue;
import edu.stanford.protege.webprotege.frame.State;
import org.semanticweb.owlapi.model.OWLAnnotation;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.Collections;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-14
 */
public class Annotation2PropertyValueTranslator {


    @Inject
    public Annotation2PropertyValueTranslator() {
    }

    @Nonnull
    public Set<PlainPropertyValue> translate(@Nonnull OWLAnnotation annotation,
                                             @Nonnull State state) {
        return Collections.singleton(PlainPropertyAnnotationValue.get(annotation.getProperty(),
                                                                      annotation.getValue(),
                                                                      state));

    }
}
