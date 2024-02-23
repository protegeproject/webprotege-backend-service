package edu.stanford.protege.webprotege.forms;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.axiom.AxiomSubjectProvider;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.index.AxiomsByReferenceIndex;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.project.ProjectDetailsManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-21
 */
public class GetEntityDeprecationFormsActionHandler extends AbstractProjectActionHandler<GetEntityDeprecationFormsAction, GetEntityDeprecationFormsResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntityFormManager entityFormManager;

    @Nonnull
    private final AxiomsByReferenceIndex axiomsByReferenceIndex;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final AxiomSubjectProvider axiomSubjectProvider;

    @Nonnull
    private final ProjectDetailsManager projectDetailsManager;

    @Inject
    public GetEntityDeprecationFormsActionHandler(@Nonnull ProjectId projectId,
                                                  @Nonnull AccessManager accessManager,
                                                  @Nonnull EntityFormManager entityFormManager,
                                                  @Nonnull AxiomsByReferenceIndex axiomsByReferenceIndex,
                                                  @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                                  @Nonnull AxiomSubjectProvider axiomSubjectProvider,
                                                  @Nonnull ProjectDetailsManager projectDetailsManager) {
        super(accessManager);
        this.projectId = checkNotNull(projectId);
        this.entityFormManager = checkNotNull(entityFormManager);
        this.axiomsByReferenceIndex = checkNotNull(axiomsByReferenceIndex);
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.axiomSubjectProvider = checkNotNull(axiomSubjectProvider);
        this.projectDetailsManager = projectDetailsManager;
    }

    @Nonnull
    @Override
    public Class<GetEntityDeprecationFormsAction> getActionClass() {
        return GetEntityDeprecationFormsAction.class;
    }

    @Nonnull
    @Override
    public GetEntityDeprecationFormsResult execute(@Nonnull GetEntityDeprecationFormsAction action,
                                                   @Nonnull ExecutionContext executionContext) {
        var entityCreationForms = entityFormManager.getFormDescriptors(action.getEntity(),
                                                                       action.projectId(),
                                                                       FormPurpose.ENTITY_DEPRECATION);

        throw new RuntimeException("Needs fixing");
//        var formDtoTranslatorComponent = projectComponent.getFormDescriptorDtoTranslatorComponent(new EntityFrameFormDataModule());
//        var formDtoTranslator = formDtoTranslatorComponent.getFormDescriptorDtoTranslator();
//        var formDtos = entityCreationForms.stream()
//                                          .map(formDtoTranslator::toFormDescriptorDto)
//                                          .collect(toImmutableList());
//        var referencesCount = projectOntologiesIndex.getOntologyIds()
//                .flatMap(ontId -> axiomsByReferenceIndex.getReferencingAxioms(Collections.singleton(action.getEntity()),
//                                                                          ontId))
//                .filter(ax -> !hasSubject(action.getEntity(), ax))
//                .count();
//        var criteria = projectDetailsManager.getProjectDetails(projectId)
//                .getEntityDeprecationSettings()
//                .getReplacedByFilter()
//                .orElse(null);
//        return new GetEntityDeprecationFormsResult(formDtos, referencesCount, criteria);
    }

    private boolean hasSubject(@Nonnull OWLEntity entity, OWLAxiom ax) {
        return axiomSubjectProvider.getSubject(ax).map(s -> s.equals(entity) || s.equals(entity.getIRI())).orElse(false);
    }
}
