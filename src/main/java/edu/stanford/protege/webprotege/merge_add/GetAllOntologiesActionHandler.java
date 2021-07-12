package edu.stanford.protege.webprotege.merge_add;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.merge.ProjectOntologiesBuilder;
import edu.stanford.protege.webprotege.project.Ontology;
import edu.stanford.protege.webprotege.upload.UploadedOntologiesCache;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.project.ProjectId;
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

public class GetAllOntologiesActionHandler extends AbstractProjectActionHandler<GetAllOntologiesAction, GetAllOntologiesResult> {
    private static final Logger logger = LoggerFactory.getLogger(NewOntologyMergeAddActionHandler.class);

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final UploadedOntologiesCache uploadedOntologiesCache;

    @Nonnull
    private final ProjectOntologiesBuilder projectOntologiesBuilder;

    @Inject
    public GetAllOntologiesActionHandler(@Nonnull AccessManager accessManager, @Nonnull ProjectId projectId, @Nonnull UploadedOntologiesCache uploadedOntologiesCache, @Nonnull ProjectOntologiesBuilder projectOntologiesBuilder) {
        super(accessManager);
        this.projectId = projectId;
        this.uploadedOntologiesCache = uploadedOntologiesCache;
        this.projectOntologiesBuilder = projectOntologiesBuilder;
    }

    @Nonnull
    @Override
    public GetAllOntologiesResult execute(@Nonnull GetAllOntologiesAction action, @Nonnull ExecutionContext executionContext){
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

            return GetAllOntologiesResult.create(list);
        }
        catch (Exception e){
            logger.info("An error occurred while merging(adding axioms) ontologies", e);
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    @Override
    public Class<GetAllOntologiesAction> getActionClass(){
        return GetAllOntologiesAction.class;
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions(GetAllOntologiesAction action) {
        return Arrays.asList(EDIT_ONTOLOGY, UPLOAD_AND_MERGE_ADDITIONS);
    }
}