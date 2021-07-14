package edu.stanford.protege.webprotege.mansyntax.render;

import edu.stanford.protege.webprotege.index.*;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntologyID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class OwlOntologyFacadeFactory {


    private final OntologyAnnotationsIndex p2;

    private final OntologySignatureIndex p3;

    private final OntologyAxiomsIndex p4;

    private final EntitiesInOntologySignatureByIriIndex p5;

    private final AnnotationAssertionAxiomsBySubjectIndex p6;

    private final OWLDataFactory p7;

    private final SubAnnotationPropertyAxiomsBySubPropertyIndex p8;

    private final AnnotationPropertyDomainAxiomsIndex p9;

    private final AnnotationPropertyRangeAxiomsIndex p10;

    private final SubClassOfAxiomsBySubClassIndex p11;

    private final AxiomsByReferenceIndex p12;

    private final EquivalentClassesAxiomsIndex p13;

    private final DisjointClassesAxiomsIndex p14;

    private final AxiomsByTypeIndex p15;

    private final SubObjectPropertyAxiomsBySubPropertyIndex p16;

    private final ObjectPropertyDomainAxiomsIndex p17;

    private final ObjectPropertyRangeAxiomsIndex p18;

    private final InverseObjectPropertyAxiomsIndex p19;

    private final EquivalentObjectPropertiesAxiomsIndex p20;

    private final DisjointObjectPropertiesAxiomsIndex p21;

    private final SubDataPropertyAxiomsBySubPropertyIndex p22;

    private final DataPropertyDomainAxiomsIndex p23;

    private final DataPropertyRangeAxiomsIndex p24;

    private final EquivalentDataPropertiesAxiomsIndex p25;

    private final DisjointDataPropertiesAxiomsIndex p26;

    private final ClassAssertionAxiomsByIndividualIndex p27;

    private final ClassAssertionAxiomsByClassIndex p28;

    private final DataPropertyAssertionAxiomsBySubjectIndex p29;

    private final ObjectPropertyAssertionAxiomsBySubjectIndex p30;

    private final SameIndividualAxiomsIndex p31;

    private final DifferentIndividualsAxiomsIndex p32;

    public OwlOntologyFacadeFactory(OntologyAnnotationsIndex p2,
                                    OntologySignatureIndex p3,
                                    OntologyAxiomsIndex p4,
                                    EntitiesInOntologySignatureByIriIndex p5,
                                    AnnotationAssertionAxiomsBySubjectIndex p6,
                                    OWLDataFactory p7,
                                    SubAnnotationPropertyAxiomsBySubPropertyIndex p8,
                                    AnnotationPropertyDomainAxiomsIndex p9,
                                    AnnotationPropertyRangeAxiomsIndex p10,
                                    SubClassOfAxiomsBySubClassIndex p11,
                                    AxiomsByReferenceIndex p12,
                                    EquivalentClassesAxiomsIndex p13,
                                    DisjointClassesAxiomsIndex p14,
                                    AxiomsByTypeIndex p15,
                                    SubObjectPropertyAxiomsBySubPropertyIndex p16,
                                    ObjectPropertyDomainAxiomsIndex p17,
                                    ObjectPropertyRangeAxiomsIndex p18,
                                    InverseObjectPropertyAxiomsIndex p19,
                                    EquivalentObjectPropertiesAxiomsIndex p20,
                                    DisjointObjectPropertiesAxiomsIndex p21,
                                    SubDataPropertyAxiomsBySubPropertyIndex p22,
                                    DataPropertyDomainAxiomsIndex p23,
                                    DataPropertyRangeAxiomsIndex p24,
                                    EquivalentDataPropertiesAxiomsIndex p25,
                                    DisjointDataPropertiesAxiomsIndex p26,
                                    ClassAssertionAxiomsByIndividualIndex p27,
                                    ClassAssertionAxiomsByClassIndex p28,
                                    DataPropertyAssertionAxiomsBySubjectIndex p29,
                                    ObjectPropertyAssertionAxiomsBySubjectIndex p30,
                                    SameIndividualAxiomsIndex p31,
                                    DifferentIndividualsAxiomsIndex p32) {
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        this.p5 = p5;
        this.p6 = p6;
        this.p7 = p7;
        this.p8 = p8;
        this.p9 = p9;
        this.p10 = p10;
        this.p11 = p11;
        this.p12 = p12;
        this.p13 = p13;
        this.p14 = p14;
        this.p15 = p15;
        this.p16 = p16;
        this.p17 = p17;
        this.p18 = p18;
        this.p19 = p19;
        this.p20 = p20;
        this.p21 = p21;
        this.p22 = p22;
        this.p23 = p23;
        this.p24 = p24;
        this.p25 = p25;
        this.p26 = p26;
        this.p27 = p27;
        this.p28 = p28;
        this.p29 = p29;
        this.p30 = p30;
        this.p31 = p31;
        this.p32 = p32;
    }

    public OwlOntologyFacade create(OWLOntologyID ontologyID) {
        return new OwlOntologyFacade(ontologyID,
                                     p2,
                                     p3,
                                     p4,
                                     p5,
                                     p6,
                                     p7,
                                     p8,
                                     p9,
                                     p10,
                                     p11,
                                     p12,
                                     p13,
                                     p14,
                                     p15,
                                     p16,
                                     p17,
                                     p18,
                                     p19,
                                     p20,
                                     p21,
                                     p22,
                                     p23,
                                     p24,
                                     p25,
                                     p26,
                                     p27,
                                     p28,
                                     p29,
                                     p30,
                                     p31,
                                     p32);
    }
}
