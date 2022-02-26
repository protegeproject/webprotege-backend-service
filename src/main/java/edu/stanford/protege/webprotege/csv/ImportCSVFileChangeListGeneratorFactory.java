package edu.stanford.protege.webprotege.csv;

import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class ImportCSVFileChangeListGeneratorFactory {

    private final OWLDataFactory dataFactory;

    private final DefaultOntologyIdManager defaultOntologyManager;

    public ImportCSVFileChangeListGeneratorFactory(OWLDataFactory dataFactory,
                                                   DefaultOntologyIdManager defaultOntologyManager) {
        this.dataFactory = dataFactory;
        this.defaultOntologyManager = defaultOntologyManager;
    }

    public ImportCSVFileChangeListGenerator create(ChangeRequestId changeRequestId, OWLClass rootClass,
                                                   CSVGrid csvGrid,
                                                   CSVImportDescriptor csvImportDescriptor) {
        return new ImportCSVFileChangeListGenerator(changeRequestId, rootClass,
                                                    csvGrid,
                                                    csvImportDescriptor,
                                                    dataFactory,
                                                    defaultOntologyManager);
    }
}
