package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.databind.JsonNode;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.forms.data.FormData;
import edu.stanford.protege.webprotege.forms.json.FormControlDataConverter;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.Optional;

public class GetEntityFormAsJsonActionHandler extends AbstractProjectActionHandler<GetEntityFormAsJsonAction, GetEntityFormAsJsonResult> {


    private final FormControlDataConverter formControlDataConverter;

    private final FormDataByFormId formDataByFormId;

    @Inject
    public GetEntityFormAsJsonActionHandler(@NotNull AccessManager accessManager, FormControlDataConverter formControlDataConverter, FormDataByFormId formDataByFormId) {
        super(accessManager);
        this.formControlDataConverter = formControlDataConverter;
        this.formDataByFormId = formDataByFormId;
    }

    @NotNull
    @Override
    public Class<GetEntityFormAsJsonAction> getActionClass() {
        return GetEntityFormAsJsonAction.class;
    }

    @NotNull
    @Override
    public GetEntityFormAsJsonResult execute(@NotNull GetEntityFormAsJsonAction action, @NotNull ExecutionContext executionContext) {
        Optional<FormData> formData = formDataByFormId.getFormData(action.formId());
        if(formData.isPresent()) {
            JsonNode jsonNode = formControlDataConverter.convert(formData.get());
            return new GetEntityFormAsJsonResult(jsonNode);
        }
        return null;
    }
}
