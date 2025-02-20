package edu.stanford.protege.webprotege.object;

import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObject;

import jakarta.inject.Inject;
import java.util.Comparator;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
public class OWLObjectComparatorImpl implements Comparator<OWLObject> {

    private final RenderingManager renderer;

    @Inject
    public OWLObjectComparatorImpl(RenderingManager renderer) {
        this.renderer = renderer;
    }

    @Override
    public int compare(OWLObject owlObject, OWLObject owlObject2) {
        if(owlObject == owlObject2) {
            return 0;
        }
        String rendering = getRendering(owlObject);
        String rendering2 = getRendering(owlObject2);
        return rendering.compareToIgnoreCase(rendering2);
    }


    private String getRendering(OWLObject object) {
        if(object instanceof IRI) {
            OWLAnnotationProperty property = DataFactory.getOWLAnnotationProperty((IRI) object);
            return renderer.getShortForm(property);
        }
        else if(object instanceof OWLEntity) {
            return renderer.getShortForm((OWLEntity) object);
        }
        else {
            return renderer.getBrowserText(object);
        }
    }
}
