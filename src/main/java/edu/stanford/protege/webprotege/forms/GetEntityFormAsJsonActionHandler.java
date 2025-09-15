package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.databind.JsonNode;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.forms.data.FormDataDto;
import edu.stanford.protege.webprotege.forms.data.FormEntitySubject;
import edu.stanford.protege.webprotege.forms.json.FormControlDataConverter;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.IRI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import javax.inject.Inject;
import java.util.Optional;

public class GetEntityFormAsJsonActionHandler extends AbstractProjectActionHandler<GetEntityFormAsJsonAction, GetEntityFormAsJsonResult> {

    private final static Logger LOGGER = LoggerFactory.getLogger(GetEntityFormAsJsonActionHandler.class);
    private final FormControlDataConverter formControlDataConverter;

    private final EntityFrameFormDataDtoBuilderFactory entityFrameFormDataDtoBuilderFactory;

    private final EntityFormRepository entityFormRepository;

    private final ApplicationContext context;


    @Inject
    public GetEntityFormAsJsonActionHandler(@NotNull AccessManager accessManager, FormControlDataConverter formControlDataConverter, EntityFrameFormDataDtoBuilderFactory entityFrameFormDataDtoBuilderFactory, EntityFormRepository entityFormRepository, ApplicationContext context) {
        super(accessManager);
        this.formControlDataConverter = formControlDataConverter;
        this.entityFrameFormDataDtoBuilderFactory = entityFrameFormDataDtoBuilderFactory;
        this.entityFormRepository = entityFormRepository;
        this.context = context;
    }

    @NotNull
    @Override
    public Class<GetEntityFormAsJsonAction> getActionClass() {
        return GetEntityFormAsJsonAction.class;
    }

    @NotNull
    @Override
    public GetEntityFormAsJsonResult execute(@NotNull GetEntityFormAsJsonAction action, @NotNull ExecutionContext executionContext) {
        Optional<FormDescriptor> formDescriptor = entityFormRepository.findFormDescriptor(action.projectId(), FormId.get(action.formId()));

        if(formDescriptor.isPresent()) {
            EntityFrameFormDataDtoBuilder builder = entityFrameFormDataDtoBuilderFactory.getFormDataDtoBuilder(this.context, new EntityFormDataRequestSpec(new EntityFormDataRequestSpec.FormRootSubject(new OWLClassImpl(IRI.create(action.entityIri())))));
            FormDataDto formDataDto = builder.toFormData(Optional.of(FormEntitySubject.get(new OWLClassImpl(IRI.create(action.entityIri())))), formDescriptor.get());
            JsonNode jsonNode = formControlDataConverter.convert(formDataDto.toFormData());
            LOGGER.debug("Responding to entity {}", jsonNode.toPrettyString());
            return new GetEntityFormAsJsonResult(jsonNode);
        }
        return new GetEntityFormAsJsonResult(null);
    }

}
