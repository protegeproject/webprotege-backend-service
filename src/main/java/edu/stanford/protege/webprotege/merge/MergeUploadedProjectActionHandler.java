package edu.stanford.protege.webprotege.merge;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.project.UploadedOntologiesCache;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;

import static edu.stanford.protege.webprotege.access.BuiltInAction.EDIT_ONTOLOGY;
import static edu.stanford.protege.webprotege.access.BuiltInAction.UPLOAD_AND_MERGE;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class MergeUploadedProjectActionHandler extends AbstractProjectActionHandler<MergeUploadedProjectAction, MergeUploadedProjectResult> {

    @Nonnull
    private final OntologyPatcher ontologyPatcher;

    @Nonnull
    private final ProjectOntologiesBuilder projectOntologiesBuilder;

    @Nonnull
    private final UploadedOntologiesCache uploadedOntologiesCache;

    @Nonnull
    private final ModifiedProjectOntologiesCalculatorFactory diffCalculatorFactory;

    @Inject
    public MergeUploadedProjectActionHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull OntologyPatcher ontologyPatcher,
                                             @Nonnull ProjectOntologiesBuilder projectOntologiesBuilder,
                                             @Nonnull UploadedOntologiesCache uploadedOntologiesCache,
                                             @Nonnull ModifiedProjectOntologiesCalculatorFactory modifiedProjectOntologiesCalculatorFactory) {
        super(accessManager);
        this.ontologyPatcher = ontologyPatcher;
        this.projectOntologiesBuilder = projectOntologiesBuilder;
        this.uploadedOntologiesCache = uploadedOntologiesCache;
        this.diffCalculatorFactory = modifiedProjectOntologiesCalculatorFactory;
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions(MergeUploadedProjectAction action) {
        return Arrays.asList(UPLOAD_AND_MERGE, EDIT_ONTOLOGY);
    }

    @Nonnull
    @Override
    public MergeUploadedProjectResult execute(@Nonnull MergeUploadedProjectAction action,
                                              @Nonnull ExecutionContext executionContext) {
            var documentId = action.uploadedDocumentId();
            var uploadedOntologies = uploadedOntologiesCache.getUploadedOntologies(documentId);
            var projectOntologies = projectOntologiesBuilder.buildProjectOntologies();
            var diffCalculator = diffCalculatorFactory.create(projectOntologies, uploadedOntologies);
            var ontologyDiffSet = diffCalculator.getModifiedOntologyDiffs();
            var commitMessage = action.commitMessage();
            ontologyPatcher.applyPatch(action.changeRequestId(), ontologyDiffSet, commitMessage, executionContext);

        return MergeUploadedProjectResult.create();
    }

    @Nonnull
    @Override
    public Class<MergeUploadedProjectAction> getActionClass() {
        return MergeUploadedProjectAction.class;
    }

}
