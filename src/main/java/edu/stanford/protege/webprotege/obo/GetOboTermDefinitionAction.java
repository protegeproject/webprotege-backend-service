package edu.stanford.protege.webprotege.obo;


import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Jun 2017
 */
public class GetOboTermDefinitionAction implements ProjectAction<GetOboTermDefinitionResult> {

    private ProjectId projectId;

    private OWLEntity term;


    private GetOboTermDefinitionAction() {
    }

    private GetOboTermDefinitionAction(ProjectId projectId, OWLEntity term) {
        this.projectId = checkNotNull(projectId);
        this.term = checkNotNull(term);
    }

    public static GetOboTermDefinitionAction create(ProjectId projectId, OWLEntity term) {
        return new GetOboTermDefinitionAction(projectId, term);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public OWLEntity getTerm() {
        return term;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GetOboTermDefinitionAction)) {
            return false;
        }
        GetOboTermDefinitionAction that = (GetOboTermDefinitionAction) o;
        return Objects.equals(projectId, that.projectId) && Objects.equals(term, that.term);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, term);
    }

    @Override
    public String toString() {
        return "GetOboTermDefinitionAction{" + "projectId=" + projectId + ", term=" + term + '}';
    }
}
