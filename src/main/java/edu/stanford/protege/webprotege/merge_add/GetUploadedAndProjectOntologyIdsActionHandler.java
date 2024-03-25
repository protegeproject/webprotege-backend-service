package edu.stanford.protege.webprotege.merge_add;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.merge.ProjectOntologiesBuilder;
import edu.stanford.protege.webprotege.project.Ontology;
import edu.stanford.protege.webprotege.project.UploadedOntologiesCache;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static edu.stanford.protege.webprotege.access.BuiltInAction.EDIT_ONTOLOGY;
import static edu.stanford.protege.webprotege.access.BuiltInAction.UPLOAD_AND_MERGE_ADDITIONS;

public class GetUploadedAndProjectOntologyIdsActionHandler extends AbstractProjectActionHandler<GetUploadedAndProjectOntologyIdsAction, GetUploadedAndProjectOntologyIdsResult> {
    private static final Logger logger = LoggerFactory.getLogger(MergeOntologiesActionHandler.class);

    @Nonnull
    private final ProjectId projectId;

    private UploadedOntologiesCache uploadedOntologiesCache;

    @Nonnull
    private final ProjectOntologiesBuilder projectOntologiesBuilder;

    @Inject
    public GetUploadedAndProjectOntologyIdsActionHandler(@Nonnull AccessManager accessManager, @Nonnull ProjectId projectId, @Nonnull UploadedOntologiesCache uploadedOntologiesCache, @Nonnull ProjectOntologiesBuilder projectOntologiesBuilder) {
        super(accessManager);
        this.projectId = projectId;
        this.uploadedOntologiesCache = uploadedOntologiesCache;
        this.projectOntologiesBuilder = projectOntologiesBuilder;
    }

    @Nonnull
    @Override
    public GetUploadedAndProjectOntologyIdsResult execute(@Nonnull GetUploadedAndProjectOntologyIdsAction action, @Nonnull ExecutionContext executionContext){
        try{
            var documentId = action.getDocumentId();

            var uploadedOntologies = uploadedOntologiesCache.getUploadedOntologies(documentId);
            var projectOntologies = projectOntologiesBuilder.buildProjectOntologies();

            List<OWLOntologyID> list = new ArrayList<>();

            for (Ontology o:projectOntologies){
                list.add(o.getOntologyId());
            }

            for (Ontology o : uploadedOntologies){
                list.add(o.getOntologyId());
            }

            return GetUploadedAndProjectOntologyIdsResult.create(list);
        }
        catch (Exception e){
            logger.info("An error occurred while merging(adding axiomsSource) ontologies", e);
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    @Override
    public Class<GetUploadedAndProjectOntologyIdsAction> getActionClass(){
        return GetUploadedAndProjectOntologyIdsAction.class;
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions(GetUploadedAndProjectOntologyIdsAction action) {
        return Arrays.asList(EDIT_ONTOLOGY, UPLOAD_AND_MERGE_ADDITIONS);
    }
}
