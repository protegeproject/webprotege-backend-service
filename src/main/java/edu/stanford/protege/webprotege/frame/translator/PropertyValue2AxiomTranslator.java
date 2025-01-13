package edu.stanford.protege.webprotege.frame.translator;

import edu.stanford.protege.webprotege.frame.Mode;
import edu.stanford.protege.webprotege.frame.PlainPropertyValue;
import edu.stanford.protege.webprotege.frame.State;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.Collections;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-03
 */
public class PropertyValue2AxiomTranslator {

    @Inject
    public PropertyValue2AxiomTranslator() {
    }

    @Nonnull
    public Set<OWLAxiom> getAxioms(OWLEntity subject,
                                   PlainPropertyValue propertyValue,
                                   Mode mode) {
        if (propertyValue.getState() == State.DERIVED) {
            return Collections.emptySet();
        }
        PropertyValueTranslator translator = new PropertyValueTranslator(subject, mode);
        return propertyValue.accept(translator);
    }
}
