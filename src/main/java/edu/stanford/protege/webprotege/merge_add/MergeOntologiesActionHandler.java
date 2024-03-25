package edu.stanford.protege.webprotege.merge_add;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.merge.ProjectOntologiesBuilder;
import edu.stanford.protege.webprotege.project.UploadedOntologiesCache;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static edu.stanford.protege.webprotege.access.BuiltInAction.EDIT_ONTOLOGY;
import static edu.stanford.protege.webprotege.access.BuiltInAction.UPLOAD_AND_MERGE_ADDITIONS;

public class MergeOntologiesActionHandler extends AbstractProjectActionHandler<MergeOntologiesAction, MergeOntologiesResult> {

    private static final Logger logger = LoggerFactory.getLogger(MergeOntologiesActionHandler.class);


    @Nonnull
    private final ProjectOntologiesBuilder projectOntologiesBuilder;

    @Nonnull
    private final UploadedOntologiesCache uploadedOntologiesCache;

    @Nonnull
    private final MergeOntologyCalculator mergeCalculator;

    @Nonnull
    private final OntologyMergeAddPatcher patcher;

    @Inject
    public MergeOntologiesActionHandler(@Nonnull AccessManager accessManager,
                                        @Nonnull ProjectOntologiesBuilder projectOntologiesBuilder,
                                        @Nonnull UploadedOntologiesCache uploadedOntologiesCache,
                                        @Nonnull HasApplyChanges changeManager) {
        super(accessManager);
        this.projectOntologiesBuilder = projectOntologiesBuilder;
        this.uploadedOntologiesCache = uploadedOntologiesCache;
        this.mergeCalculator = new MergeOntologyCalculator();
        this.patcher = new OntologyMergeAddPatcher(changeManager);
    }

    @Nonnull
    @Override
    public MergeOntologiesResult execute(@Nonnull MergeOntologiesAction action,
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

            return MergeOntologiesResult.create();
        }
        catch (Exception e){
            logger.info("An error occurred while merging(adding axiomsSource) ontologies", e);
            throw new RuntimeException(e);
        }
    }


    @Nonnull
    @Override
    public Class<MergeOntologiesAction> getActionClass(){
        return MergeOntologiesAction.class;
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions(MergeOntologiesAction action) {
        return Arrays.asList(EDIT_ONTOLOGY, UPLOAD_AND_MERGE_ADDITIONS);
    }
}
