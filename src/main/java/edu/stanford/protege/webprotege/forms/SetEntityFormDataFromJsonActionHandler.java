package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectChangeHandler;
import edu.stanford.protege.webprotege.forms.data.FormData;
import edu.stanford.protege.webprotege.forms.data.FormDataDto;
import edu.stanford.protege.webprotege.forms.data.FormEntitySubject;
import edu.stanford.protege.webprotege.forms.json.Json2FormData;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

public class SetEntityFormDataFromJsonActionHandler extends AbstractProjectChangeHandler<OWLEntity, SetEntityFormDataFromJsonAction, SetEntityFormDataFromJsonResult> {

    private final EntityFormRepository entityFormRepository;

    private final Json2FormData json2FormData;

    private final EntityFrameFormDataDtoBuilderFactory entityFrameFormDataDtoBuilderFactory;

    private final ApplicationContext applicationContext;

    private final EntityFormChangeListGeneratorFactory changeListGeneratorFactory;


    public SetEntityFormDataFromJsonActionHandler(@NotNull AccessManager accessManager,
                                                  @NotNull HasApplyChanges applyChanges,
                                                  EntityFormRepository entityFormRepository,
                                                  Json2FormData json2FormData,
                                                  EntityFrameFormDataDtoBuilderFactory entityFrameFormDataDtoBuilderFactory,
                                                  ApplicationContext applicationContext, EntityFormChangeListGeneratorFactory changeListGeneratorFactory) {
        super(accessManager, applyChanges);
        this.entityFormRepository = entityFormRepository;
        this.json2FormData = json2FormData;
        this.entityFrameFormDataDtoBuilderFactory = entityFrameFormDataDtoBuilderFactory;
        this.applicationContext = applicationContext;
        this.changeListGeneratorFactory = changeListGeneratorFactory;
    }

    @NotNull
    @Override
    public Class<SetEntityFormDataFromJsonAction> getActionClass() {
        return SetEntityFormDataFromJsonAction.class;
    }

    @Override
    protected ChangeListGenerator<OWLEntity> getChangeListGenerator(SetEntityFormDataFromJsonAction action, ExecutionContext executionContext) {
        EntityFrameFormDataDtoBuilder builder = entityFrameFormDataDtoBuilderFactory.getFormDataDtoBuilder(applicationContext, new EntityFormDataRequestSpec());
        Optional<FormDescriptor> formDescriptor = entityFormRepository.findFormDescriptor(action.projectId(), action.formId());
        if(formDescriptor.isPresent()) {
            Optional<FormData> editedFormData = json2FormData.convert(action.owlEntity(), action.jsonFormData(), formDescriptor.get());
            if(editedFormData.isPresent()){
                FormDataDto formDataDto = builder.toFormData(Optional.of(FormEntitySubject.get(action.owlEntity())), formDescriptor.get());
                var pristineFormsData =  ImmutableMap.of(action.formId(), formDataDto.toFormData());
                var editedFormsData = new FormDataByFormId(ImmutableMap.of(action.formId(), editedFormData.get()));
                FormDataUpdateSanityChecker.check(pristineFormsData, editedFormsData);
                return changeListGeneratorFactory.create(action.changeRequestId(),
                        action.owlEntity(),
                        pristineFormsData,
                        editedFormsData);
            }

        }
        return null;
    }

    @Override
    protected SetEntityFormDataFromJsonResult createActionResult(ChangeApplicationResult<OWLEntity> changeApplicationResult, SetEntityFormDataFromJsonAction action, ExecutionContext executionContext) {
        return new SetEntityFormDataFromJsonResult();
    }
}
