package edu.stanford.protege.webprotege.csv;

import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;
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

    public static final String CHANNEL = "webprotege.csv.ImportCsvFile";


    private final ChangeRequestId changeRequestId;

    private final ProjectId projectId;

    private final OWLClass importRootClass;

    private final DocumentId documentId;

    private final CSVImportDescriptor descriptor;

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    public ImportCSVFileAction(ChangeRequestId changeRequestId,
                               ProjectId projectId,
                               DocumentId csvDocumentId,
                               OWLClass importRootClass,
                               CSVImportDescriptor descriptor) {
        this.changeRequestId = changeRequestId;
        this.projectId = checkNotNull(projectId);
        this.importRootClass = checkNotNull(importRootClass);
        this.documentId = checkNotNull(csvDocumentId);
        this.descriptor = checkNotNull(descriptor);
    }

    public ChangeRequestId changeRequestId() {
        return changeRequestId;
    }

    @Nonnull
    @Override
    public ProjectId projectId() {
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
