package edu.stanford.protege.webprotege.merge_add;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.merge.ProjectOntologiesBuilder;
import edu.stanford.protege.webprotege.project.UploadedOntologiesCache;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static edu.stanford.protege.webprotege.access.BuiltInAction.EDIT_ONTOLOGY;
import static edu.stanford.protege.webprotege.access.BuiltInAction.UPLOAD_AND_MERGE_ADDITIONS;

public class ExistingOntologyMergeAddActionHandler extends AbstractProjectActionHandler<ExistingOntologyMergeAddAction, ExistingOntologyMergeAddResult> {

    private static final Logger logger = LoggerFactory.getLogger(MergeOntologiesActionHandler.class);

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final UploadedOntologiesCache uploadedOntologiesCache;

    @Nonnull
    private final ProjectOntologiesBuilder projectOntologiesBuilder;


    private final MergeOntologyCalculator mergeCalculator;

    private final OntologyMergeAddPatcher patcher;

    @Inject
    public ExistingOntologyMergeAddActionHandler(@Nonnull AccessManager accessManager,
                                                 @Nonnull ProjectId projectId,
                                                 @Nonnull UploadedOntologiesCache uploadedOntologiesCache,
                                                 @Nonnull ProjectOntologiesBuilder projectOntologiesBuilder,
                                                 @Nonnull HasApplyChanges changeManager) {
        super(accessManager);
        this.projectId = projectId;
        this.uploadedOntologiesCache = uploadedOntologiesCache;
        this.projectOntologiesBuilder = projectOntologiesBuilder;
        this.mergeCalculator = new MergeOntologyCalculator();
        this.patcher = new OntologyMergeAddPatcher(changeManager);
    }


    @Nonnull
    @Override
    public ExistingOntologyMergeAddResult execute(@Nonnull ExistingOntologyMergeAddAction action,
                                                  @Nonnull ExecutionContext executionContext){
        try {
            var documentId = action.getDocumentId();

            var uploadedOntologies = uploadedOntologiesCache.getUploadedOntologies(documentId);
            var projectOntologies = projectOntologiesBuilder.buildProjectOntologies();

            var ontologyList = action.getSelectedOntologies();

            var axioms = mergeCalculator.getMergeAxioms(projectOntologies, uploadedOntologies, ontologyList);
            var annotations = mergeCalculator.getMergeAnnotations(projectOntologies, uploadedOntologies, ontologyList);

            OWLOntologyID ontologyID = action.getTargetOntology();

            List<OntologyChange> changes = patcher.addAxiomsAndAnnotations(axioms, annotations, ontologyID);

            patcher.applyChanges(changes, executionContext);

            return new ExistingOntologyMergeAddResult();

        }catch (Exception e){
            logger.info("An error occurred while merging(adding axiomsSource) ontologies", e);
            throw new RuntimeException(e);
        }

    }

    @Nonnull
    @Override
    public Class<ExistingOntologyMergeAddAction> getActionClass(){
        return ExistingOntologyMergeAddAction.class;
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions(ExistingOntologyMergeAddAction action) {
        return Arrays.asList(EDIT_ONTOLOGY, UPLOAD_AND_MERGE_ADDITIONS);
    }
}
