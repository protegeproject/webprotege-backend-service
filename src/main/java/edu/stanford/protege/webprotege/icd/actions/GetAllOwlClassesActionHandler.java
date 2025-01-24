package edu.stanford.protege.webprotege.icd.actions;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.index.ProjectAxiomsSignatureIndex;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedObject;

import javax.inject.Inject;
import java.util.List;

public class GetAllOwlClassesActionHandler extends AbstractProjectActionHandler<GetAllOwlClassesAction, GetAllOwlClassesResult> {

    private final ProjectAxiomsSignatureIndex projectAxiomsSignatureIndex;

    @Inject
    public GetAllOwlClassesActionHandler(@NotNull AccessManager accessManager, ProjectAxiomsSignatureIndex projectAxiomsSignatureIndex) {
        super(accessManager);
        this.projectAxiomsSignatureIndex = projectAxiomsSignatureIndex;
    }

    @NotNull
    @Override
    public Class<GetAllOwlClassesAction> getActionClass() {
        return GetAllOwlClassesAction.class;
    }

    @NotNull
    @Override
    public GetAllOwlClassesResult execute(@NotNull GetAllOwlClassesAction action, @NotNull ExecutionContext executionContext) {
        List<IRI> allClasses = projectAxiomsSignatureIndex.getProjectAxiomsSignature(EntityType.CLASS).map(OWLNamedObject::getIRI).toList();
        return new GetAllOwlClassesResult(allClasses);
    }
}
