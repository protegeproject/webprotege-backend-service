package edu.stanford.protege.webprotege.csv;

import com.google.common.base.Charsets;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectChangeHandler;
import edu.stanford.protege.webprotege.inject.UploadsDirectory;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static edu.stanford.protege.webprotege.access.BuiltInAction.EDIT_ONTOLOGY;
import static java.util.Collections.singletonList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/05/2013
 */
public class ImportCSVFileActionHandler extends AbstractProjectChangeHandler<Integer,ImportCSVFileAction, ImportCSVFileResult> {

    @Nonnull
    private final File uploadsDirectory;

    @Nonnull
    private final ImportCSVFileChangeListGeneratorFactory factory;

    @Inject
    public ImportCSVFileActionHandler(@Nonnull AccessManager accessManager,
                                      @Nonnull HasApplyChanges applyChanges,
                                      @Nonnull @UploadsDirectory File uploadsDirectory,
                                      @Nonnull ImportCSVFileChangeListGeneratorFactory factory) {
        super(accessManager, applyChanges);
        this.uploadsDirectory = uploadsDirectory;
        this.factory = factory;
    }

    @Nonnull
    @Override
    public Class<ImportCSVFileAction> getActionClass() {
        return ImportCSVFileAction.class;
    }

    @Override
    protected ChangeListGenerator<Integer> getChangeListGenerator(ImportCSVFileAction action,
                                                                  ExecutionContext executionContext) {
        CSVGrid csvGrid = parseCSVGrid(action);
        var rootClass = action.getImportRootClass();
        var csvImportDescriptor = action.getDescriptor();
        return factory.create(action.changeRequestId(), rootClass,
                              csvGrid,
                              csvImportDescriptor);
    }

    private CSVGrid parseCSVGrid(ImportCSVFileAction action) {
        try {
            CSVGridParser parser = new CSVGridParser();
            final File file = new File(uploadsDirectory, action.getDocumentId().getDocumentId());
            final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),
                                                                                   Charsets.UTF_8));
            CSVGrid grid = parser.readAll(reader);
            reader.close();
            return grid;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected ImportCSVFileResult createActionResult(ChangeApplicationResult<Integer> changeApplicationResult,
                                                     ImportCSVFileAction action,
                                                     ExecutionContext executionContext) {
        return new ImportCSVFileResult(changeApplicationResult.getSubject());
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions(ImportCSVFileAction action) {
        return singletonList(EDIT_ONTOLOGY);
    }
}
