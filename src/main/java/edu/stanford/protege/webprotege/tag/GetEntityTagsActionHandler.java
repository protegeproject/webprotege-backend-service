package edu.stanford.protege.webprotege.tag;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Mar 2018
 */
public class GetEntityTagsActionHandler extends AbstractProjectActionHandler<GetEntityTagsAction, GetEntityTagsResult> {

    @Nonnull
    private final TagsManager tagsManager;

    @Inject
    public GetEntityTagsActionHandler(@Nonnull AccessManager accessManager,
                                      @Nonnull TagsManager tagsManager) {
        super(accessManager);
        this.tagsManager = checkNotNull(tagsManager);
    }

    @Nonnull
    @Override
    public Class<GetEntityTagsAction> getActionClass() {
        return GetEntityTagsAction.class;
    }

    @Nonnull
    @Override
    public GetEntityTagsResult execute(@Nonnull GetEntityTagsAction action, @Nonnull ExecutionContext executionContext) {
        ProjectId projectId = action.projectId();
        OWLEntity entity = action.entity();
        return new GetEntityTagsResult(tagsManager.getTags(entity),
                                       tagsManager.getProjectTags());
    }
}
