package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.entity.EntityNode;
import edu.stanford.protege.webprotege.index.*;
import edu.stanford.protege.webprotege.msg.MessageFormatter;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import org.semanticweb.owlapi.model.OWLDataFactory;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class MoveEntityChangeListGeneratorFactory {

    private final OWLDataFactory p2;

    private final MessageFormatter p3;

    private final ProjectOntologiesIndex p4;

    private final DefaultOntologyIdManager p5;

    private final EquivalentClassesAxiomsIndex p6;

    private final SubClassOfAxiomsBySubClassIndex p7;

    private final SubObjectPropertyAxiomsBySubPropertyIndex p8;

    private final SubDataPropertyAxiomsBySubPropertyIndex p9;

    private final SubAnnotationPropertyAxiomsBySubPropertyIndex p10;

    public MoveEntityChangeListGeneratorFactory(OWLDataFactory p2,
                                                MessageFormatter p3,
                                                ProjectOntologiesIndex p4,
                                                DefaultOntologyIdManager p5,
                                                EquivalentClassesAxiomsIndex p6,
                                                SubClassOfAxiomsBySubClassIndex p7,
                                                SubObjectPropertyAxiomsBySubPropertyIndex p8,
                                                SubDataPropertyAxiomsBySubPropertyIndex p9,
                                                SubAnnotationPropertyAxiomsBySubPropertyIndex p10) {
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        this.p5 = p5;
        this.p6 = p6;
        this.p7 = p7;
        this.p8 = p8;
        this.p9 = p9;
        this.p10 = p10;
    }

    public MoveEntityChangeListGenerator create(Path<EntityNode> fromNodePath,
                                                Path<EntityNode> toNodeParentPath,
                                                DropType dropType,
                                                ChangeRequestId changeRequestId,
                                                String commitMessage) {
        return new MoveEntityChangeListGenerator(fromNodePath,
                                                 toNodeParentPath,
                                                 dropType,
                                                 changeRequestId,
                                                 p2,
                                                 p3,
                                                 p4,
                                                 p5,
                                                 p6,
                                                 p7,
                                                 p8,
                                                 p9,
                                                 p10, commitMessage);
    }
}
