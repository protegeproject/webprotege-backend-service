package edu.stanford.bmir.protege.web.server.ontology;

import com.google.auto.value.AutoValue;

import edu.stanford.bmir.protege.web.server.entity.ObjectData;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/07/15
 */
@AutoValue

public abstract class OntologyIdData extends ObjectData {

    public static OntologyIdData get(@Nonnull OWLOntologyID ontologyID,
                                     @Nonnull String browserText) {
        return new AutoValue_OntologyIdData(browserText, ontologyID);
    }

    @Nonnull
    @Override
    public abstract OWLOntologyID getObject();

    @Override
    public String getUnquotedBrowserText() {
        return getBrowserText();
    }
}
