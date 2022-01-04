package edu.stanford.protege.webprotege.axioms;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.change.AddAxiomChange;
import edu.stanford.protege.webprotege.change.FixedChangeListGenerator;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.change.RemoveAxiomChange;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.download.DownloadFormat;
import edu.stanford.protege.webprotege.ipc.CommandExecutionException;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-09-01
 */
public class AddAxiomsDelegateHandler extends AbstractProjectActionHandler<AddAxiomsRequest, AddAxiomsResponse> {

    private final ChangeManager changeManager;

    private final DefaultOntologyIdManager defaultOntologyIdManager;

    public AddAxiomsDelegateHandler(AccessManager accessManager,
                                    ChangeManager changeManager,
                                    DefaultOntologyIdManager defaultOntologyIdManager) {
        super(accessManager);
        this.changeManager = changeManager;
        this.defaultOntologyIdManager = defaultOntologyIdManager;
    }


    @Nonnull
    @Override
    public Class<AddAxiomsRequest> getActionClass() {
        return AddAxiomsRequest.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(AddAxiomsRequest action) {
        return BuiltInAction.EDIT_ONTOLOGY;
    }

    @Nonnull
    @Override
    public AddAxiomsResponse execute(@Nonnull AddAxiomsRequest action, @Nonnull ExecutionContext executionContext) {
        var projectId = action.projectId();
        var loader = new AxiomsDocumentLoader(projectId,
                                              action.ontologyDocument(),
                                              action.mimeType(),
                                              defaultOntologyIdManager.getDefaultOntologyId());
        var changes = loader.<OntologyChange>loadAxioms((ax, ontologyId) -> new AddAxiomChange(ontologyId, ax));
        var result = changeManager.applyChanges(executionContext.getUserId(),
                                                new FixedChangeListGenerator<>(changes, "", action.commitMessage()));
        var appliedChangesCount = result.getChangeList().size();
        return new AddAxiomsResponse(projectId,
                                     appliedChangesCount);
    }

}
