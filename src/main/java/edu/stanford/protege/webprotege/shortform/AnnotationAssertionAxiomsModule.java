package edu.stanford.protege.webprotege.shortform;

import edu.stanford.protege.webprotege.index.*;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-13
 */
public class AnnotationAssertionAxiomsModule {

    private final ProjectOntologiesIndex projectOntologiesIndex;

    private final ProjectAnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubjectIndex;

    private final ProjectSignatureIndex provideProjectSignatureIndex;

    private final EntitiesInProjectSignatureIndex entitiesInProjectSignatureIndex;

    private final EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex;

    private final AnnotationAssertionAxiomsByValueIndex annotationAssertionAxiomsByValueIndex;

    public AnnotationAssertionAxiomsModule(ProjectOntologiesIndex projectOntologiesIndex,
                                           ProjectAnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubjectIndex,
                                           ProjectSignatureIndex provideProjectSignatureIndex,
                                           EntitiesInProjectSignatureIndex entitiesInProjectSignatureIndex,
                                           EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex,
                                           AnnotationAssertionAxiomsByValueIndex annotationAssertionAxiomsByValueIndex) {
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.annotationAssertionAxiomsBySubjectIndex = annotationAssertionAxiomsBySubjectIndex;
        this.provideProjectSignatureIndex = provideProjectSignatureIndex;
        this.entitiesInProjectSignatureIndex = entitiesInProjectSignatureIndex;
        this.entitiesInProjectSignatureByIriIndex = entitiesInProjectSignatureByIriIndex;
        this.annotationAssertionAxiomsByValueIndex = annotationAssertionAxiomsByValueIndex;
    }

    
    @ProjectSingleton
    ProjectAnnotationAssertionAxiomsBySubjectIndex provideAnnotationAssertionAxiomsBySubjectIndex() {
        return annotationAssertionAxiomsBySubjectIndex;
    }

    
    @ProjectSingleton
    public ProjectSignatureIndex getProvideProjectSignatureIndex() {
        return provideProjectSignatureIndex;
    }

    
    @ProjectSingleton
    public EntitiesInProjectSignatureIndex provideEntitiesInProjectSignatureIndex() {
        return entitiesInProjectSignatureIndex;
    }

    
    @ProjectSingleton
    public AnnotationAssertionAxiomsByValueIndex provideAnnotationAssertionAxiomsByValueIndex() {
        return annotationAssertionAxiomsByValueIndex;
    }

    
    @ProjectSingleton
    public EntitiesInProjectSignatureByIriIndex provideEntitiesInProjectSignatureByIriIndex() {
        return entitiesInProjectSignatureByIriIndex;
    }

    
    @ProjectSingleton
    public ProjectOntologiesIndex provideProjectOntologiesIndex() {
        return projectOntologiesIndex;
    }
}
