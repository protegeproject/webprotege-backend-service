package edu.stanford.protege.webprotege.renderer;

import org.semanticweb.owlapi.model.OWLObject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/15
 */
public interface HasHtmlBrowserText {

    String getHtmlBrowserText(OWLObject object);
}
