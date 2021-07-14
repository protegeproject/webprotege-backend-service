package edu.stanford.protege.webprotege.form;



import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.entity.EntityRenamer;
import edu.stanford.protege.webprotege.form.data.FormData;
import edu.stanford.protege.webprotege.form.data.FormDataDto;
import edu.stanford.protege.webprotege.form.data.FormEntitySubject;
import edu.stanford.protege.webprotege.form.data.FormSubject;
import edu.stanford.protege.webprotege.form.field.FormFieldDeprecationStrategy;
import edu.stanford.protege.webprotege.index.*;
import edu.stanford.protege.webprotege.inject.ProjectComponent;
import edu.stanford.protege.webprotege.msg.MessageFormatter;
import edu.stanford.protege.webprotege.owlapi.RenameMap;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import edu.stanford.protege.webprotege.project.ProjectId;
import edu.stanford.protege.webprotege.projectsettings.EntityDeprecationSettings;
import edu.stanford.protege.webprotege.util.EntityDeleter;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLAxiomVisitorExAdapter;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableMap.toImmutableMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-22
 */
public class DeprecateEntityByFormChangeListGenerator implements ChangeListGenerator<OWLEntity> {

    @Nonnull
    private final OWLEntity entityToBeDeprecated;

    @Nonnull
    private final Optional<FormData> deprecationFormData;

    @Nonnull
    private final Optional<OWLEntity> replacementEntity;

    private EntityDeprecationSettings entityDeprecationSettings;

    private EntityDeleter entityDeleter;

    @Nonnull
    private final EntityFormChangeListGeneratorFactory formChangeListGeneratorFactory;

    @Nonnull
    private final EntityFormManager entityFormManager;

    @Nonnull
    private final MessageFormatter messageFormatter;

    @Nonnull
    private String message = "";

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ProjectComponent projectComponent;

    @Nonnull
    private final EntityRenamer entityRenamer;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final SubClassOfAxiomsBySubClassIndex subClassOfAxiomsBySubClassIndex;

    @Nonnull
    private final SubObjectPropertyAxiomsBySubPropertyIndex subObjectPropertyAxiomsBySubPropertyIndex;

    @Nonnull
    private final SubDataPropertyAxiomsBySubPropertyIndex subDataPropertyAxiomsBySubPropertyIndex;

    @Nonnull
    private final SubAnnotationPropertyAxiomsBySubPropertyIndex subAnnotationPropertyAxiomsBySubPropertyIndex;

    @Nonnull
    private final ClassAssertionAxiomsByIndividualIndex classAssertionAxiomsByIndividualIndex;



    public DeprecateEntityByFormChangeListGenerator(@Nonnull OWLEntity entityToBeDeprecated,
                                                    @Nonnull Optional<FormData> deprecationFormData,
                                                    @Nonnull Optional<OWLEntity> replacementEntity,
                                                    @Nonnull EntityDeprecationSettings entityDeprecationSettings,
                                                    @Nonnull EntityDeleter entityDeleter,
                                                    @Nonnull EntityFormChangeListGeneratorFactory formChangeListGeneratorFactory,
                                                    @Nonnull EntityFormManager entityFormManager,
                                                    @Nonnull MessageFormatter messageFormatter,
                                                    @Nonnull ProjectId projectId,
                                                    @Nonnull ProjectComponent projectComponent,
                                                    @Nonnull EntityRenamer entityRenamer,
                                                    @Nonnull DefaultOntologyIdManager defaultOntologyIdManager,
                                                    @Nonnull OWLDataFactory dataFactory,
                                                    @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                                    @Nonnull SubClassOfAxiomsBySubClassIndex subClassOfAxiomsBySubClassIndex,
                                                    @Nonnull SubObjectPropertyAxiomsBySubPropertyIndex subObjectPropertyAxiomsBySubPropertyIndex,
                                                    @Nonnull SubDataPropertyAxiomsBySubPropertyIndex subDataPropertyAxiomsBySubPropertyIndex,
                                                    @Nonnull SubAnnotationPropertyAxiomsBySubPropertyIndex subAnnotationPropertyAxiomsBySubPropertyIndex,
                                                    @Nonnull ClassAssertionAxiomsByIndividualIndex classAssertionAxiomsByIndividualIndex) {
        this.entityToBeDeprecated = checkNotNull(entityToBeDeprecated);
        this.deprecationFormData = checkNotNull(deprecationFormData);
        this.replacementEntity = checkNotNull(replacementEntity);
        this.entityDeprecationSettings = checkNotNull(entityDeprecationSettings);
        this.entityDeleter = entityDeleter;
        this.formChangeListGeneratorFactory = formChangeListGeneratorFactory;
        this.entityFormManager = entityFormManager;
        this.messageFormatter = messageFormatter;
        this.projectId = projectId;
        this.projectComponent = projectComponent;
        this.entityRenamer = entityRenamer;
        this.defaultOntologyIdManager = defaultOntologyIdManager;
        this.dataFactory = dataFactory;
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.subClassOfAxiomsBySubClassIndex = subClassOfAxiomsBySubClassIndex;
        this.subObjectPropertyAxiomsBySubPropertyIndex = subObjectPropertyAxiomsBySubPropertyIndex;
        this.subDataPropertyAxiomsBySubPropertyIndex = subDataPropertyAxiomsBySubPropertyIndex;
        this.subAnnotationPropertyAxiomsBySubPropertyIndex = subAnnotationPropertyAxiomsBySubPropertyIndex;
        this.classAssertionAxiomsByIndividualIndex = classAssertionAxiomsByIndividualIndex;
    }

    @Override
    public OntologyChangeList<OWLEntity> generateChanges(ChangeGenerationContext context) {
        message = messageFormatter.format("Deprecated {0}", entityToBeDeprecated);

        // Note that the order of these changes is important

        var changeListBuilder = ImmutableList.<OntologyChange>builder();

        addChangesToReplaceOrDeleteEntity(context, changeListBuilder);

        // Add the stuff we want to preserve to the deprecated entity.  This will cancel out
        // any previous deletions
        addChangesToPreserveFieldsOnEntity(context, changeListBuilder);

        addChangesToRemoveParents(changeListBuilder);

        addChangesToReparentEntity(changeListBuilder);

        addChangesToMarkEntityAsDeprecated(changeListBuilder);

        addChangesToAddDeprecationFormData(context, changeListBuilder);

        return OntologyChangeList.<OWLEntity>builder()
                .addAll(changeListBuilder.build())
                .build(entityToBeDeprecated);
    }

    private void addChangesToReplaceOrDeleteEntity(ChangeGenerationContext context,
                                                   ImmutableList.Builder<OntologyChange> changeListBuilder) {
        if (replacementEntity.isPresent()) {
            // Replace usages of the deprecated entity with other replacement entity
            // This effectively removes the deprecated entity from the ontology
            addChangesToReplaceEntity(context, changeListBuilder);
            addChangesToAddReplacedWithAnnotation(changeListBuilder);
        }
        else {
            // Temporarily delete the entity so that it gets properly cleaned up.  Preserved things
            // will be restored
            entityDeleter.getChangesToDeleteEntities(Collections.singleton(entityToBeDeprecated));
        }
    }

    private void addChangesToReparentEntity(ImmutableList.Builder<OntologyChange> changeListBuilder) {
        // If the deprecated parent is specified then reparent
        var defaultOntologyId = defaultOntologyIdManager.getDefaultOntologyId();
        getPlacementAxiom().ifPresent(ax -> changeListBuilder.add(AddAxiomChange.of(defaultOntologyId, ax)));
    }

    private void addChangesToMarkEntityAsDeprecated(ImmutableList.Builder<OntologyChange> changeListBuilder) {
        OWLOntologyID defaultOntologyId = defaultOntologyIdManager.getDefaultOntologyId();
        // Mark the deprecated entity as deprecated with an annotation
        changeListBuilder.add(AddAxiomChange.of(defaultOntologyId,
                                                dataFactory.getOWLDeclarationAxiom(entityToBeDeprecated)));
        // Make sure it's also declared, otherwise
        // it will just be a deprecated IRI and the type information will be lost
        changeListBuilder.add(AddAxiomChange.of(defaultOntologyId,
                                                dataFactory.getDeprecatedOWLAnnotationAssertionAxiom(
                                                        entityToBeDeprecated.getIRI())));
    }

    private Optional<OWLAxiom> getPlacementAxiom() {
        return entityToBeDeprecated.accept(new OWLEntityVisitorEx<>() {
            @Nonnull
            @Override
            public Optional<OWLAxiom> visit(@Nonnull OWLClass cls) {
                return entityDeprecationSettings.getDeprecatedClassesParent()
                                                .map(parent -> dataFactory.getOWLSubClassOfAxiom(cls, parent));
            }

            @Nonnull
            @Override
            public Optional<OWLAxiom> visit(@Nonnull OWLObjectProperty property) {
                return entityDeprecationSettings.getDeprecatedObjectPropertiesParent()
                                                .map(parent -> dataFactory.getOWLSubObjectPropertyOfAxiom(property,
                                                                                                          parent));
            }

            @Nonnull
            @Override
            public Optional<OWLAxiom> visit(@Nonnull OWLDataProperty property) {
                return entityDeprecationSettings.getDeprecatedDataPropertiesParent()
                                                .map(parent -> dataFactory.getOWLSubDataPropertyOfAxiom(property,
                                                                                                        parent));
            }

            @Nonnull
            @Override
            public Optional<OWLAxiom> visit(@Nonnull OWLNamedIndividual individual) {
                return entityDeprecationSettings.getDeprecatedIndividualsParent()
                                                .map(parent -> dataFactory.getOWLClassAssertionAxiom(parent,
                                                                                                     individual));
            }

            @Nonnull
            @Override
            public Optional<OWLAxiom> visit(@Nonnull OWLDatatype datatype) {
                return Optional.empty();
            }

            @Nonnull
            @Override
            public Optional<OWLAxiom> visit(@Nonnull OWLAnnotationProperty property) {
                return entityDeprecationSettings.getDeprecatedAnnotationPropertiesParent()
                                                .map(parent -> dataFactory.getOWLSubAnnotationPropertyOfAxiom(property,
                                                                                                              parent));
            }
        });
    }

    private void addChangesToPreserveFieldsOnEntity(ChangeGenerationContext context,
                                                    ImmutableList.Builder<OntologyChange> changeListBuilder) {

        var formDescriptors = entityFormManager.getFormDescriptors(entityToBeDeprecated,
                                                                   projectId,
                                                                   FormPurpose.ENTITY_EDITING);

        var preservedFormDescriptors = formDescriptors.stream()
                                                      .map(formDescriptor -> formDescriptor.withFields(field -> field.getDeprecationStrategy()
                                                                                                                     .equals(FormFieldDeprecationStrategy.LEAVE_VALUES_INTACT)))
                                                      .collect(toImmutableList());
        var formDataToPreserve = getFormData(preservedFormDescriptors, entityToBeDeprecated);
        var changes = formChangeListGeneratorFactory.createForAdd(entityToBeDeprecated, new FormDataByFormId(formDataToPreserve))
                                                    .generateChanges(context)
                                                    .getChanges();
        changeListBuilder.addAll(changes);
    }

    private void addChangesToCleanUpReplacementEntity(ChangeGenerationContext context,
                                                      @Nonnull ImmutableList.Builder<OntologyChange> changesListBuilder) {

        if (replacementEntity.isEmpty()) {
            return;
        }

        // Remove all of the stuff on the deprecated entity
        var formDescriptorsForDeprecatedEntity = entityFormManager.getFormDescriptors(entityToBeDeprecated,
                                                                                      projectId,
                                                                                      FormPurpose.ENTITY_EDITING);

        // First remove all fields that are on the deprecated entity from the replacement entity that are there
        // as a result of the rename
        var formDataOnDeprecatedEntity = getFormData(formDescriptorsForDeprecatedEntity, entityToBeDeprecated);
        // Map this data to the replacement entity so that we can remove it from the replacement entity
        var formDataOnReplacementToRemove = formDataOnDeprecatedEntity.values()
                                                                      .stream()
                                                                      .map(formData -> FormData.get(Optional.<FormEntitySubject>of(
                                                                              FormEntitySubject.get(replacementEntity.get())),
                                                                                                    formData.getFormDescriptor(),
                                                                                                    formData.getFormFieldData()))
                                                                      .collect(toImmutableMap(FormData::getFormId,
                                                                                              formData -> formData));

        // Generate the changes to remove the deprecated form data from the replacement entity
        changesListBuilder.addAll(formChangeListGeneratorFactory.createForRemove(replacementEntity.get(),
                                                                                 formDataOnReplacementToRemove)
                                                                .generateChanges(context)
                                                                .getChanges());

        // Make sure that we have not inadvertently removed any form data from the replacement.  We do
        // this by adding back in all of the fields on the replacement entity.  Changes that add back existing
        // data will happily be ignored
        var formDescriptorsFormReplacementEntity = entityFormManager.getFormDescriptors(replacementEntity.get(),
                                                                                        projectId,
                                                                                        FormPurpose.ENTITY_EDITING);
        var formDataForReplacement = getFormData(formDescriptorsFormReplacementEntity, replacementEntity.get());
        changesListBuilder.addAll(formChangeListGeneratorFactory.createForAdd(replacementEntity.get(), new FormDataByFormId(formDataForReplacement))
                                                                .generateChanges(context)
                                                                .getChanges());


    }

    private void addChangesToAddDeprecationFormData(@Nonnull ChangeGenerationContext context,
                                                    @Nonnull ImmutableList.Builder<OntologyChange> changeBuilder) {
        if (deprecationFormData.isEmpty()) {
            return;
        }
        formChangeListGeneratorFactory.createForAdd(entityToBeDeprecated,
                                                    new FormDataByFormId(ImmutableMap.of(deprecationFormData.get().getFormId(),
                                                                                         deprecationFormData.get())))
                                      .generateChanges(context)
                                      .getChanges()
                                      .forEach(changeBuilder::add);
    }

    private void addChangesToAddReplacedWithAnnotation(ImmutableList.Builder<OntologyChange> changeListBuilder) {
        replacementEntity.flatMap(replacement -> {
            return entityDeprecationSettings.getReplacedByPropertyIri().map(iri -> {
                var prop = DataFactory.getOWLAnnotationProperty(iri);
                var value = replacement.getIRI();
                var annotationAx = DataFactory.get()
                                              .getOWLAnnotationAssertionAxiom(prop,
                                                                              entityToBeDeprecated.getIRI(),
                                                                              value);
                return AddAxiomChange.of(defaultOntologyIdManager.getDefaultOntologyId(), annotationAx);
            });
        }).ifPresent(changeListBuilder::add);


    }

    private void addChangesToReplaceEntity(@Nonnull ChangeGenerationContext context,
                                           @Nonnull ImmutableList.Builder<OntologyChange> changeListBuilder) {
        if (replacementEntity.isEmpty()) {
            return;
        }
        var replacement = replacementEntity.get();
        var changes = entityRenamer.generateChanges(ImmutableMap.of(entityToBeDeprecated, replacement.getIRI()));
        changes.stream().filter(chg -> chg.accept(new OntologyChangeVisitorEx<>() {
            @Override
            public Boolean getDefaultReturnValue() {
                return true;
            }

            @Override
            public Boolean visit(@Nonnull AddAxiomChange addAxiomChange) {
                // Don't reposition the replacement entity into the position of the deprecated entity
                return addAxiomChange.getAxiom().accept(new OWLAxiomVisitorExAdapter<>(true) {
                    @Nonnull
                    @Override
                    public Boolean visit(OWLSubClassOfAxiom axiom) {
                        return !(axiom.getSubClass().equals(replacement) && axiom.getSuperClass().isNamed());
                    }
                });
            }
        })).forEach(changeListBuilder::add);
        // Clean up the replacement entity by removing anything from the deprecated entity
        // that was copied over by the above find-and-replace
        addChangesToCleanUpReplacementEntity(context, changeListBuilder);
    }

    private void addChangesToRemoveParents(ImmutableList.Builder<OntologyChange> changeBuilder) {
        entityToBeDeprecated.accept(new OWLEntityVisitor() {
            @Override
            public void visit(@Nonnull OWLClass cls) {
                projectOntologiesIndex.getOntologyIds()
                                      .flatMap(ontId -> subClassOfAxiomsBySubClassIndex.getSubClassOfAxiomsForSubClass(
                                              cls,
                                              ontId)
                                                                                       .filter(ax -> ax.getSuperClass()
                                                                                                       .isNamed())
                                                                                       .map(ax -> RemoveAxiomChange.of(
                                                                                               ontId,
                                                                                               ax)))
                                      .forEach(changeBuilder::add);
            }

            @Override
            public void visit(@Nonnull OWLObjectProperty property) {
                projectOntologiesIndex.getOntologyIds()
                                      .flatMap(ontId -> subObjectPropertyAxiomsBySubPropertyIndex.getSubPropertyOfAxioms(
                                              property,
                                              ontId).map(ax -> RemoveAxiomChange.of(ontId, ax)))
                                      .forEach(changeBuilder::add);
            }

            @Override
            public void visit(@Nonnull OWLDataProperty property) {
                projectOntologiesIndex.getOntologyIds()
                                      .flatMap(ontId -> subDataPropertyAxiomsBySubPropertyIndex.getSubPropertyOfAxioms(
                                              property,
                                              ontId).map(ax -> RemoveAxiomChange.of(ontId, ax)))
                                      .forEach(changeBuilder::add);
            }

            @Override
            public void visit(@Nonnull OWLNamedIndividual individual) {
                projectOntologiesIndex.getOntologyIds()
                                      .flatMap(ontId -> classAssertionAxiomsByIndividualIndex.getClassAssertionAxioms(
                                              individual,
                                              ontId)
                                                                                             .filter(ax -> ax.getClassExpression()
                                                                                                             .isNamed())
                                                                                             .map(ax -> RemoveAxiomChange
                                                                                                     .of(ontId, ax)))
                                      .forEach(changeBuilder::add);
            }

            @Override
            public void visit(@Nonnull OWLDatatype datatype) {
            }

            @Override
            public void visit(@Nonnull OWLAnnotationProperty property) {
                projectOntologiesIndex.getOntologyIds()
                                      .flatMap(ontId -> subAnnotationPropertyAxiomsBySubPropertyIndex.getSubPropertyOfAxioms(
                                              property,
                                              ontId).map(ax -> RemoveAxiomChange.of(ontId, ax)))
                                      .forEach(changeBuilder::add);
            }
        });
    }


    private ImmutableMap<FormId, FormData> getFormData(ImmutableList<FormDescriptor> formDescriptors,
                                                       OWLEntity subject) {
        return formDescriptors.stream()
                              .map(fd -> getFormData(fd, subject))
                              .collect(toImmutableMap(FormDataDto::getFormId, FormDataDto::toFormData));


    }

    private FormDataDto getFormData(FormDescriptor formDescriptor, OWLEntity subject) {
        return projectComponent.getEntityFrameFormDataComponentBuilder(new EntityFrameFormDataModule())
                               .formDataBuilder()
                               .toFormData(Optional.of(FormSubject.get(subject)), formDescriptor);
    }

    @Override
    public OWLEntity getRenamedResult(OWLEntity result, RenameMap renameMap) {
        return renameMap.getRenamedEntity(result);
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<OWLEntity> result) {
        return message;
    }
}
