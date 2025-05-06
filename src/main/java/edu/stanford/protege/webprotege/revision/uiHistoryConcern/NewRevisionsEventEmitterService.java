package edu.stanford.protege.webprotege.revision.uiHistoryConcern;

import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildren;
import edu.stanford.protege.webprotege.revision.Revision;
import org.semanticweb.owlapi.model.IRI;

import java.util.Optional;

public interface NewRevisionsEventEmitterService<E> {
    void emitNewRevisionsEvent(Optional<Revision> revision,
                               ChangeRequestId changeRequestId,
                               E subject);

    void emitNewProjectOrderedChildrenEvent(IRI entityParentIri,
                                            Optional<ProjectOrderedChildren> initialOrderedChildrenOptional,
                                            Optional<ProjectOrderedChildren> newOrderedChildrenOptional,
                                            UserId userId,
                                            ChangeRequestId changeRequestId,
                                            String commitMessage);
}
