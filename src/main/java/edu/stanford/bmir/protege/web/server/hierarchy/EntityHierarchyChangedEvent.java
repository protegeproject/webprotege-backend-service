package edu.stanford.bmir.protege.web.server.hierarchy;



import edu.stanford.bmir.protege.web.server.event.ProjectEvent;
import edu.stanford.bmir.protege.web.server.project.ProjectId;

import javax.annotation.Nonnull;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 1 Dec 2017
 */
public class EntityHierarchyChangedEvent extends ProjectEvent<EntityHierarchyChangedHandler> {

    private HierarchyId hierarchyId;

    private GraphModelChangedEvent changeEvent;

    public EntityHierarchyChangedEvent(@Nonnull ProjectId source,
                                       @Nonnull HierarchyId hierarchyId,
                                       @Nonnull GraphModelChangedEvent changeEvent) {
        super(source);
        this.hierarchyId = checkNotNull(hierarchyId);
        this.changeEvent = checkNotNull(changeEvent);
    }

    @Nonnull
    public HierarchyId getHierarchyId() {
        return hierarchyId;
    }


    private EntityHierarchyChangedEvent() {
    }

    @Override
    protected void dispatch(EntityHierarchyChangedHandler handler) {
        handler.handleHierarchyChanged(this);
    }

    public GraphModelChangedEvent getChangeEvent() {
        return changeEvent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntityHierarchyChangedEvent)) {
            return false;
        }
        EntityHierarchyChangedEvent that = (EntityHierarchyChangedEvent) o;
        return hierarchyId.equals(that.hierarchyId) && changeEvent.equals(that.changeEvent) && getSource().equals(that.getSource());
    }

    @Override
    public int hashCode() {
        return Objects.hash(hierarchyId, changeEvent, getSource());
    }
}
