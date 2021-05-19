package edu.stanford.protege.webprotege.mansyntax.render;

import edu.stanford.protege.webprotege.index.AnnotationPropertyRangeAxiomsIndex;
import edu.stanford.protege.webprotege.index.EntitiesInProjectSignatureByIriIndex;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/02/2014
 */
public class AnnotationPropertyRangeSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLAnnotationProperty, OWLAnnotationPropertyRangeAxiom, OWLObject> {

    @Nonnull
    private final AnnotationPropertyRangeAxiomsIndex axiomsIndex;

    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex entitiesByIri;

    @Inject
    public AnnotationPropertyRangeSectionRenderer(@Nonnull AnnotationPropertyRangeAxiomsIndex axiomsIndex,
                                                  @Nonnull EntitiesInProjectSignatureByIriIndex entitiesByIri) {
        this.axiomsIndex = checkNotNull(axiomsIndex);
        this.entitiesByIri = checkNotNull(entitiesByIri);
    }

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.RANGE;
    }

    @Override
    protected Set<OWLAnnotationPropertyRangeAxiom> getAxiomsInOntology(OWLAnnotationProperty subject,
                                                                       OWLOntologyID ontologyId) {
        return axiomsIndex.getAnnotationPropertyRangeAxioms(subject, ontologyId).collect(toSet());
    }


    @Override
    public List<OWLObject> getRenderablesForItem(OWLAnnotationProperty subject,
                                                 OWLAnnotationPropertyRangeAxiom item,
                                                 OWLOntologyID ontologyId) {
        return entitiesByIri.getEntitiesInSignature(item.getRange()).collect(toList());
    }
}
