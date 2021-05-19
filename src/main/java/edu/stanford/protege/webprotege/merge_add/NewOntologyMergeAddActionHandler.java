package edu.stanford.protege.webprotege.merge_add;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.merge.ProjectOntologiesBuilder;
import edu.stanford.protege.webprotege.upload.UploadedOntologiesCache;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static edu.stanford.protege.webprotege.access.BuiltInAction.*;

public class NewOntologyMergeAddActionHandler extends AbstractProjectActionHandler<NewOntologyMergeAddAction, NewOntologyMergeAddResult> {

    private static final Logger logger = LoggerFactory.getLogger(NewOntologyMergeAddActionHandler.class);

    @Nonnull
    private final UploadedOntologiesCache uploadedOntologiesCache;

    @Nonnull
    private final ProjectOntologiesBuilder projectOntologiesBuilder;

    @Nonnull
    private final MergeOntologyCalculator mergeCalculator;

    @Nonnull
    private final OntologyMergeAddPatcher patcher;

    @Inject
    public NewOntologyMergeAddActionHandler(@Nonnull AccessManager accessManager,
                                            @Nonnull UploadedOntologiesCache uploadedOntologiesCache,
                                            @Nonnull ProjectOntologiesBuilder projectOntologiesBuilder,
                                            @Nonnull HasApplyChanges changeManager) {
        super(accessManager);
        this.uploadedOntologiesCache = uploadedOntologiesCache;
        this.projectOntologiesBuilder = projectOntologiesBuilder;
        this.mergeCalculator = new MergeOntologyCalculator();
        this.patcher = new OntologyMergeAddPatcher(changeManager);
    }

    @Nonnull
    @Override
    public NewOntologyMergeAddResult execute(@Nonnull NewOntologyMergeAddAction action,
                                             @Nonnull ExecutionContext executionContext){
        try{
            var documentId = action.getDocumentId();

            var uploadedOntologies = uploadedOntologiesCache.getUploadedOntologies(documentId);
            var projectOntologies = projectOntologiesBuilder.buildProjectOntologies();

            var ontologyList = action.getOntologyList();

            var axioms = mergeCalculator.getMergeAxioms(projectOntologies, uploadedOntologies, ontologyList);
            var annotations = mergeCalculator.getMergeAnnotations(projectOntologies, uploadedOntologies, ontologyList);

            OWLOntologyID newOntologyID = new OWLOntologyID();

            List<OntologyChange> changes = patcher.addAxiomsAndAnnotations(axioms, annotations, newOntologyID);

            patcher.applyChanges(changes, executionContext);

            return NewOntologyMergeAddResult.create();
        }
        catch (Exception e){
            logger.info("An error occurred while merging(adding axioms) ontologies", e);
            throw new RuntimeException(e);
        }
    }


    @Nonnull
    @Override
    public Class<NewOntologyMergeAddAction> getActionClass(){
        return NewOntologyMergeAddAction.class;
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions(NewOntologyMergeAddAction action) {
        return Arrays.asList(EDIT_ONTOLOGY, UPLOAD_AND_MERGE_ADDITIONS);
    }
}
