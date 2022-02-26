package edu.stanford.protege.webprotege.crud;



import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.ChangeGenerationContext;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.change.OntologyChangeList;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.msg.MessageFormatter;
import edu.stanford.protege.webprotege.owlapi.RenameMap;
import edu.stanford.protege.webprotege.util.EntityDeleter;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 9 May 2017
 */

public class DeleteEntitiesChangeListGenerator implements ChangeListGenerator<Set<OWLEntity>> {

    @Nonnull
    private final EntityDeleter entityDeleter;

    @Nonnull
    private final Set<OWLEntity> entities;

    @Nonnull
    private final MessageFormatter msgFormatter;

    @Nonnull
    private final ChangeRequestId changeRequestId;

    private String message = "Deleted entities";



    public DeleteEntitiesChangeListGenerator(@Nonnull MessageFormatter msgFormatter,
                                             @Nonnull EntityDeleter entityDeleter,
                                             @Nonnull Set<OWLEntity> entities, @Nonnull ChangeRequestId changeRequestId) {
        this.entityDeleter = entityDeleter;
        this.entities = ImmutableSet.copyOf(entities);
        this.msgFormatter = msgFormatter;
        this.changeRequestId = changeRequestId;
    }

    @Override
    public ChangeRequestId getChangeRequestId() {
        return changeRequestId;
    }

    @Override
    public OntologyChangeList<Set<OWLEntity>> generateChanges(ChangeGenerationContext context) {
        generateMessage();
        var ontologyChanges = entityDeleter.getChangesToDeleteEntities(entities);
        return OntologyChangeList.<Set<OWLEntity>>builder().addAll(ontologyChanges).build(entities);
    }

    private void generateMessage() {
        if (entities.size() == 1) {
            message = msgFormatter.format("Deleted {0}: {1}",
                                          entities.iterator().next().getEntityType().getPrintName().toLowerCase(),
                                          entities);
        }
        else {
            Collection<EntityType<?>> deletedTypes = entities.stream()
                                                             .map(OWLEntity::getEntityType)
                                                             .collect(Collectors.toSet());
            if (deletedTypes.size() == 1) {
                message = msgFormatter.format("Deleted {0} {1}: {2}",
                                              entities.size(),
                                              deletedTypes.iterator().next().getPluralPrintName().toLowerCase(),
                                              entities);
            }
            else {
                message = msgFormatter.format("Deleted: {1}",
                                              entities);
            }
        }
    }

    @Override
    public Set<OWLEntity> getRenamedResult(Set<OWLEntity> result, RenameMap renameMap) {
        return renameMap.getRenamedEntities(result);
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<Set<OWLEntity>> result) {
        return message;
    }
}
