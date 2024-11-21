package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.databind.JsonNode;
import edu.stanford.protege.webprotege.common.ChangeRequest;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.ProjectRequest;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

public record SetEntityFormDataFromJsonAction(@Nonnull ChangeRequestId changeRequestId,
                                              @Nonnull ProjectId projectId,
                                              @Nonnull OWLEntity owlEntity,
                                              @Nonnull FormId formId,
                                              @Nonnull JsonNode jsonFormData) implements ProjectRequest<SetEntityFormDataFromJsonResult>, ChangeRequest {

    public static final String CHANNEL = "webprotege.forms.SetEntityFormFromJson";

    @Override
    public ChangeRequestId changeRequestId() {
        return changeRequestId;
    }

    @NotNull
    @Override
    public ProjectId projectId() {
        return projectId;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
