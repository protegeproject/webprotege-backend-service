package edu.stanford.protege.webprotege.place;

import org.semanticweb.owlapi.model.OWLClass;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 19/05/2014
 */
public class OWLClassItem extends Item<OWLClass> {

    private static final Type<OWLClass> TYPE = new Type<OWLClass>("Class");

    public static Type<OWLClass> getType() {
        return TYPE;
    }

    @Override
    public Type<OWLClass> getAssociatedType() {
        return TYPE;
    }

    public OWLClassItem(OWLClass item) {
        super(item);
    }

    @Override
    public String getItemRendering() {
        return getItem().getIRI().toQuotedString();
    }
}
