package edu.stanford.protege.webprotege.crud;

import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.ChangeGenerationContext;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.change.OntologyChangeList;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.owlapi.RenameMap;
import edu.stanford.protege.webprotege.util.EntityDeleter;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.Collections;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/03/2013
 */
public class DeleteEntityChangeListGenerator implements ChangeListGenerator<OWLEntity> {

    @Nonnull
    private final OWLEntity entity;

    @Nonnull
    private final EntityDeleter entityDeleter;

    @Nonnull
    private final ChangeRequestId changeRequestId;

    @Inject
    public DeleteEntityChangeListGenerator(@Nonnull ChangeRequestId changeRequestId,
                                           @Nonnull OWLEntity entity,
                                           @Nonnull EntityDeleter entityDeleter) {
        this.entity = checkNotNull(entity);
        this.entityDeleter = checkNotNull(entityDeleter);
        this.changeRequestId = changeRequestId;
    }

    @Override
    public ChangeRequestId getChangeRequestId() {
        return changeRequestId;
    }

    @Override
    public OntologyChangeList<OWLEntity> generateChanges(ChangeGenerationContext context) {
        var deletionChanges = entityDeleter.getChangesToDeleteEntities(Collections.singleton(entity));
        var changeListBuilder = new OntologyChangeList.Builder<OWLEntity>();
        changeListBuilder.addAll(deletionChanges);
        return changeListBuilder.build(entity);
    }

    @Override
    public OWLEntity getRenamedResult(OWLEntity result, RenameMap renameMap) {
        return renameMap.getRenamedEntity(result);
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<OWLEntity> result) {
        return "Deleted " + entity.getEntityType().getPrintName().toLowerCase();
    }
}
