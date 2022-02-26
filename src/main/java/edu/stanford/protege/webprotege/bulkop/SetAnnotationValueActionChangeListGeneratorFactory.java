package edu.stanford.protege.webprotege.bulkop;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.protege.webprotege.index.EntitiesInOntologySignatureIndex;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class SetAnnotationValueActionChangeListGeneratorFactory {

    private final OWLDataFactory dataFactory;

    private final ProjectOntologiesIndex projectOntologiesIndex;

    private final EntitiesInOntologySignatureIndex entitiesInSignature;

    private final AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubject;

    public SetAnnotationValueActionChangeListGeneratorFactory(OWLDataFactory dataFactory,
                                                              ProjectOntologiesIndex projectOntologiesIndex,
                                                              EntitiesInOntologySignatureIndex entitiesInSignature,
                                                              AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubject) {
        this.dataFactory = dataFactory;
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.entitiesInSignature = entitiesInSignature;
        this.annotationAssertionAxiomsBySubject = annotationAssertionAxiomsBySubject;
    }

    public SetAnnotationValueActionChangeListGenerator create(ChangeRequestId changeRequestId, ImmutableSet<OWLEntity> entities,
                                                              OWLAnnotationProperty property,
                                                              OWLAnnotationValue value,
                                                              String commitMessage) {
        return new SetAnnotationValueActionChangeListGenerator(changeRequestId, dataFactory,
                                                               entities,
                                                               property,
                                                               value,
                                                               commitMessage,
                                                               projectOntologiesIndex,
                                                               entitiesInSignature,
                                                               annotationAssertionAxiomsBySubject);
    }
}
