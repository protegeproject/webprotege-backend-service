package edu.stanford.bmir.protege.web.server.csv;

import edu.stanford.bmir.protege.web.server.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.server.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/05/2013
 */
public class ImportCSVFileAction implements ProjectAction<ImportCSVFileResult> {

    private ProjectId projectId;

    private OWLClass importRootClass;

    private DocumentId documentId;

    private CSVImportDescriptor descriptor;

    /**
     * For serialization only
     */
    private ImportCSVFileAction() {
    }

    public ImportCSVFileAction(ProjectId projectId, DocumentId csvDocumentId, OWLClass importRootClass, CSVImportDescriptor descriptor) {
        this.projectId = checkNotNull(projectId);
        this.importRootClass = checkNotNull(importRootClass);
        this.documentId = checkNotNull(csvDocumentId);
        this.descriptor = checkNotNull(descriptor);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public DocumentId getDocumentId() {
        return documentId;
    }

    public CSVImportDescriptor getDescriptor() {
        return descriptor;
    }

    public OWLClass getImportRootClass() {
        return importRootClass;
    }
}
