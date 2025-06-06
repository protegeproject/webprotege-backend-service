package edu.stanford.protege.webprotege.crud.icatx;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.AddAxiomChange;
import edu.stanford.protege.webprotege.change.OntologyChangeList;
import edu.stanford.protege.webprotege.common.AnnotationAssertionDictionaryLanguage;
import edu.stanford.protege.webprotege.common.DictionaryLanguage;
import edu.stanford.protege.webprotege.common.DictionaryLanguageVisitor;
import edu.stanford.protege.webprotege.crud.*;
import edu.stanford.protege.webprotege.crud.gen.GeneratedAnnotationsSettings;
import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class IcatxSuffixEntityCrudKitHandler implements EntityCrudKitHandler<IcatxSuffixSettings, ChangeSetEntityCrudSession> {

    private final static Logger LOGGER = LoggerFactory.getLogger(IcatxSuffixEntityCrudKitHandler.class);

    private final IcatxSuffixSettings icatxSuffixSettings;

    private final EntityCrudKitPrefixSettings entityCrudKitPrefixSettings;

    private final CommandExecutor<GetUniqueIdRequest, GetUniqueIdResponse> uniqueIdExecutor;

    private final OWLDataFactory dataFactory;

    @Nonnull
    private final EntityIriPrefixResolver entityIriPrefixResolver;

    public IcatxSuffixEntityCrudKitHandler(IcatxSuffixSettings icatxSuffixSettings,
                                           EntityCrudKitPrefixSettings entityCrudKitPrefixSettings,
                                           CommandExecutor<GetUniqueIdRequest, GetUniqueIdResponse> uniqueIdExecutor, OWLDataFactory dataFactory, @Nonnull EntityIriPrefixResolver entityIriPrefixResolver) {
        this.icatxSuffixSettings = icatxSuffixSettings;
        this.entityCrudKitPrefixSettings = entityCrudKitPrefixSettings;
        this.uniqueIdExecutor = uniqueIdExecutor;
        this.dataFactory = dataFactory;
        this.entityIriPrefixResolver = entityIriPrefixResolver;
    }

    @Override
    public EntityCrudKitPrefixSettings getPrefixSettings() {
        return entityCrudKitPrefixSettings;
    }

    @Override
    public IcatxSuffixSettings getSuffixSettings() {
        return icatxSuffixSettings;
    }

    @Override
    public EntityCrudKitSettings getSettings() {
        return EntityCrudKitSettings.get(entityCrudKitPrefixSettings, icatxSuffixSettings, GeneratedAnnotationsSettings.empty());
    }

    @Override
    public ChangeSetEntityCrudSession createChangeSetSession() {
        return null;
    }

    @Override
    public <E extends OWLEntity> E create(@NotNull ChangeSetEntityCrudSession session,
                                          @NotNull EntityType<E> entityType,
                                          @NotNull EntityShortForm shortForm,
                                          @NotNull Optional<String> langTag,
                                          @NotNull ImmutableList<OWLEntity> parents,
                                          @NotNull EntityCrudContext context,
                                          @NotNull OntologyChangeList.Builder<E> changeListBuilder) throws CannotGenerateFreshEntityIdException {
        var targetOntology = context.getTargetOntologyId();
        var suppliedName = shortForm.getShortForm();
        var dictionaryLanguage = context.getDictionaryLanguage();

        var iriPrefix = entityIriPrefixResolver.getIriPrefix(entityCrudKitPrefixSettings, entityType, parents);
        IRI newIri = null;
        if (iriPrefix.equalsIgnoreCase(entityCrudKitPrefixSettings.getIRIPrefix())) {
            newIri = IRI.create(entityCrudKitPrefixSettings.getIRIPrefix(), UUID.randomUUID().toString());
        } else {
            try {
                GetUniqueIdResponse response = uniqueIdExecutor.execute(new GetUniqueIdRequest(iriPrefix), new ExecutionContext()).get(5, TimeUnit.SECONDS);
                newIri = IRI.create(response.uniqueId());
            } catch (TimeoutException | InterruptedException | ExecutionException e) {
                LOGGER.error("Error fetching unique id ", e);
                throw new RuntimeException("Error fetching unique id ", e);
            }
        }

        var entity = dataFactory.getOWLEntity(entityType, newIri);
        var labellingLiteral = getLabellingLiteral(newIri.toString(), langTag, dictionaryLanguage);

        changeListBuilder.add(AddAxiomChange.of(targetOntology, dataFactory.getOWLDeclarationAxiom(entity)));

        if (!suppliedName.isBlank()) {
            dictionaryLanguage.accept(new DictionaryLanguageVisitor<Object>() {
                @Override
                public Object visit(@Nonnull AnnotationAssertionDictionaryLanguage language) {
                    var annotationPropertyIri = language.getAnnotationPropertyIri();
                    var ax = dataFactory.getOWLAnnotationAssertionAxiom(dataFactory.getOWLAnnotationProperty(annotationPropertyIri), entity.getIRI(), labellingLiteral);
                    changeListBuilder.add(AddAxiomChange.of(targetOntology, ax));
                    return null;
                }
            });

        }
        return entity;
    }

    @Override
    public EntityCrudKitId getKitId() {
        return icatxSuffixSettings.getKitId();
    }


    private OWLLiteral getLabellingLiteral(String suppliedName, Optional<String> langTag, DictionaryLanguage dictionaryLanguage) {
        return dataFactory.getOWLLiteral(suppliedName, langTag.orElse(dictionaryLanguage.getLang()));
    }
}
