package edu.stanford.protege.webprotege.index.impl;

import edu.stanford.protege.webprotege.index.AxiomsByTypeIndex;
import edu.stanford.protege.webprotege.index.DependentIndex;
import edu.stanford.protege.webprotege.index.Index;
import edu.stanford.protege.webprotege.index.InverseObjectPropertyAxiomsIndex;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-24
 */
public class InverseObjectPropertyAxiomsIndexImpl implements InverseObjectPropertyAxiomsIndex, DependentIndex {

    @Nonnull
    private final AxiomsByTypeIndex axiomsByTypeIndex;

    @Inject
    public InverseObjectPropertyAxiomsIndexImpl(@Nonnull AxiomsByTypeIndex axiomsByTypeIndex) {
        this.axiomsByTypeIndex = checkNotNull(axiomsByTypeIndex);
    }

    @Nonnull
    @Override
    public Collection<Index> getDependencies() {
        return List.of(axiomsByTypeIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLInverseObjectPropertiesAxiom> getInverseObjectPropertyAxioms(@Nonnull OWLObjectProperty property,
                                                                                  @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(ontologyId);
        checkNotNull(property);
        return axiomsByTypeIndex.getAxiomsByType(AxiomType.INVERSE_OBJECT_PROPERTIES, ontologyId)
                .filter(ax -> ax.getFirstProperty().equals(property) || ax.getSecondProperty().equals(property));
    }
}
