package edu.stanford.protege.webprotege.index.impl;

import edu.stanford.protege.webprotege.index.AxiomsByTypeIndex;
import edu.stanford.protege.webprotege.index.DependentIndex;
import edu.stanford.protege.webprotege.index.EquivalentObjectPropertiesAxiomsIndex;
import edu.stanford.protege.webprotege.index.Index;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
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
public class EquivalentObjectPropertiesAxiomsIndexImpl implements EquivalentObjectPropertiesAxiomsIndex, DependentIndex {

    @Nonnull
    private final AxiomsByTypeIndex axiomsByTypeIndex;

    @Inject
    public EquivalentObjectPropertiesAxiomsIndexImpl(@Nonnull AxiomsByTypeIndex axiomsByTypeIndex) {
        // Use AxiomsByType because there are few ontologies with many EquivalentObjectProperties axiomsSource
        this.axiomsByTypeIndex = checkNotNull(axiomsByTypeIndex);
    }

    @Nonnull
    @Override
    public Collection<Index> getDependencies() {
        return List.of(axiomsByTypeIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLEquivalentObjectPropertiesAxiom> getEquivalentObjectPropertiesAxioms(@Nonnull OWLObjectProperty property,
                                                                                          @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(ontologyId);
        checkNotNull(property);
        return axiomsByTypeIndex.getAxiomsByType(AxiomType.EQUIVALENT_OBJECT_PROPERTIES, ontologyId)
                .filter(ax -> ax.getProperties().contains(property));
    }
}
