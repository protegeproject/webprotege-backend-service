package edu.stanford.protege.webprotege.mansyntax;

import edu.stanford.protege.webprotege.change.ReverseEngineeredChangeDescriptionGeneratorFactory;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.frame.CheckManchesterSyntaxFrameAction;
import edu.stanford.protege.webprotege.frame.SetManchesterSyntaxFrameAction;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class ManchesterSyntaxChangeGeneratorFactory {

    private final ManchesterSyntaxFrameParser parser;

    private final OntologyAxiomPairChangeGenerator axiomPairChangeGenerator;

    private final ReverseEngineeredChangeDescriptionGeneratorFactory reverseEngineeredChangeDescriptionGeneratorFactory;

    public ManchesterSyntaxChangeGeneratorFactory(ManchesterSyntaxFrameParser parser,
                                                  OntologyAxiomPairChangeGenerator axiomPairChangeGenerator,
                                                  ReverseEngineeredChangeDescriptionGeneratorFactory reverseEngineeredChangeDescriptionGeneratorFactory) {
        this.parser = parser;
        this.axiomPairChangeGenerator = axiomPairChangeGenerator;
        this.reverseEngineeredChangeDescriptionGeneratorFactory = reverseEngineeredChangeDescriptionGeneratorFactory;
    }

    public ManchesterSyntaxChangeGenerator create(OWLEntityData rendering,
                                                  String fromRendering,
                                                  String toRendering,
                                                  String commitMessage,
                                                  CheckManchesterSyntaxFrameAction action) {
        return new ManchesterSyntaxChangeGenerator(parser,
                                                   axiomPairChangeGenerator,
                                                   reverseEngineeredChangeDescriptionGeneratorFactory,
                                                   rendering,
                                                   fromRendering,
                                                   toRendering,
                                                   commitMessage,
                                                   action, ChangeRequestId.generate());
    }

    public ManchesterSyntaxChangeGenerator create(OWLEntityData rendering,
                                                  String fromRendering,
                                                  String toRendering,
                                                  String commitMessage,
                                                  SetManchesterSyntaxFrameAction action) {
        return new ManchesterSyntaxChangeGenerator(parser,
                                                   axiomPairChangeGenerator,
                                                   reverseEngineeredChangeDescriptionGeneratorFactory,
                                                   rendering,
                                                   fromRendering,
                                                   toRendering,
                                                   commitMessage,
                                                   action,
                                                   action.changeRequestId());
    }
}
