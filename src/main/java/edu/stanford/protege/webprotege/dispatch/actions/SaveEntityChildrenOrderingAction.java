package edu.stanford.protege.webprotege.dispatch.actions;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import java.util.List;

public record SaveEntityChildrenOrderingAction(@Nonnull ProjectId projectId,
                                               @Nonnull IRI entityIri,
                                               @Nonnull List<String> orderedChildren) implements ProjectAction<SaveEntityChildrenOrderingResult> {

    public final static String CHANNEL = "webprotege.projects.SaveEntityChildReordering";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
