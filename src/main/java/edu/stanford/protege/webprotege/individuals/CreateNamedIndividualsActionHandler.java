package edu.stanford.protege.webprotege.individuals;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.entity.CreateNamedIndividualsAction;
import edu.stanford.protege.webprotege.entity.CreateNamedIndividualsResult;
import edu.stanford.protege.webprotege.entity.EntityNode;
import edu.stanford.protege.webprotege.entity.EntityNodeRenderer;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static edu.stanford.protege.webprotege.access.BuiltInAction.CREATE_INDIVIDUAL;
import static edu.stanford.protege.webprotege.access.BuiltInAction.EDIT_ONTOLOGY;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class CreateNamedIndividualsActionHandler extends AbstractProjectActionHandler<CreateNamedIndividualsAction, CreateNamedIndividualsResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final HasApplyChanges changeApplicator;

    @Nonnull
    private final EntityNodeRenderer renderer;

    @Nonnull
    private final CreateIndividualsChangeListGeneratorFactory factory;

    @Inject
    public CreateNamedIndividualsActionHandler(@Nonnull AccessManager accessManager,
                                               @Nonnull ProjectId projectId,
                                               @Nonnull HasApplyChanges changeApplicator,
                                               @Nonnull EntityNodeRenderer renderer,
                                               @Nonnull CreateIndividualsChangeListGeneratorFactory factory) {
        super(accessManager);
        this.projectId = checkNotNull(projectId);
        this.changeApplicator = checkNotNull(changeApplicator);
        this.renderer = checkNotNull(renderer);
        this.factory = checkNotNull(factory);
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions(CreateNamedIndividualsAction action) {
        return Arrays.asList(EDIT_ONTOLOGY, CREATE_INDIVIDUAL);
    }

    @Nonnull
    @Override
    public CreateNamedIndividualsResult execute(@Nonnull CreateNamedIndividualsAction action,
                                                @Nonnull ExecutionContext executionContext) {
        ChangeApplicationResult<Set<OWLNamedIndividual>> result = changeApplicator.applyChanges(executionContext.userId(),
                                                                                                factory.create(action.types(),
                                                                                                               action.sourceText(),
                                                                                                               action.langTag(),
                                                                                                               action.changeRequestId()));
        ImmutableSet<EntityNode> individualData = result.getSubject().stream()
                                                        .map(renderer::render)
                                                        .collect(toImmutableSet());
        return new CreateNamedIndividualsResult(projectId,
                                                individualData);
    }

    @Nonnull
    @Override
    public Class<CreateNamedIndividualsAction> getActionClass() {
        return CreateNamedIndividualsAction.class;
    }
}
