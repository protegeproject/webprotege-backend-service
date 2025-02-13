package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.change.AddAxiomChange;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.change.RemoveAxiomChange;
import edu.stanford.protege.webprotege.index.AxiomsByReferenceIndex;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.util.OWLObjectDuplicator;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-21
 */
public class EntityRenamer {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final AxiomsByReferenceIndex axiomsByReferenceIndex;

    @Inject
    public EntityRenamer(@Nonnull OWLDataFactory dataFactory,
                         @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                         @Nonnull AxiomsByReferenceIndex axiomsByReferenceIndex) {
        this.dataFactory = dataFactory;
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.axiomsByReferenceIndex = axiomsByReferenceIndex;
    }

    public List<OntologyChange> generateChanges(@Nonnull Map<OWLEntity, IRI> iriReplacementMap) {
        OWLObjectDuplicator duplicator = new OWLObjectDuplicator(iriReplacementMap, dataFactory);
        var changes = new ArrayList<OntologyChange>();
        projectOntologiesIndex.getOntologyIds().forEach(ontId -> {
            axiomsByReferenceIndex.getReferencingAxioms(iriReplacementMap.keySet(), ontId)
                                  .forEach(ax -> {
                                      var removeAxiom = RemoveAxiomChange.of(ontId, ax);
                                      changes.add(removeAxiom);
                                      OWLAxiom replacementAx = duplicator.duplicateObject(ax);
                                      var addAxiom = AddAxiomChange.of(ontId, replacementAx);
                                      changes.add(addAxiom);
                                  });
        });
        return changes;
    }
}
