package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.revision.RevisionStoreFactory;
import edu.stanford.protege.webprotege.upload.DocumentResolver;
import edu.stanford.protege.webprotege.upload.UploadedOntologiesProcessor;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class ProjectImporterFactory {

    @Nonnull
    private final UploadedOntologiesProcessor uploadedOntologiesProcessor;

    @Nonnull
    private final DocumentResolver documentResolver;

    @Nonnull
    private final RevisionStoreFactory revisionStoreFactory;

    public ProjectImporterFactory(@Nonnull UploadedOntologiesProcessor uploadedOntologiesProcessor,
                                  @Nonnull DocumentResolver documentResolver,
                                  @Nonnull RevisionStoreFactory revisionStoreFactory) {
        this.uploadedOntologiesProcessor = uploadedOntologiesProcessor;
        this.documentResolver = documentResolver;
        this.revisionStoreFactory = revisionStoreFactory;
    }

    @Nonnull
    public ProjectImporter create(@Nonnull ProjectId projectId) {
        return new ProjectImporter(projectId, uploadedOntologiesProcessor, documentResolver, revisionStoreFactory);
    }
}
