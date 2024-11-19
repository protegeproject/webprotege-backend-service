package edu.stanford.protege.webprotege.icd.actions;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.access.*;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectChangeHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.linearization.LinearizationManager;
import edu.stanford.protege.webprotege.postcoordination.PostcoordinationManager;
import org.semanticweb.owlapi.model.*;
import org.slf4j.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static edu.stanford.protege.webprotege.access.BuiltInAction.*;
import static java.util.Arrays.asList;

public class CreateClassesFromApiActionHandler extends AbstractProjectChangeHandler<Set<OWLClass>, CreateClassesFromApiAction, CreateClassesFromApiResult> {

    private final Logger logger = LoggerFactory.getLogger(CreateClassesFromApiActionHandler.class);

    @Nonnull
    private final CreateClassesChangeGeneratorFactory changeGeneratorFactory;

    @Nonnull
    private final LinearizationManager linearizationManager;

    @Nonnull
    private final PostcoordinationManager postcoordinationManager;


    @Inject
    public CreateClassesFromApiActionHandler(@Nonnull AccessManager accessManager,

                                             @Nonnull HasApplyChanges applyChanges,
                                             @Nonnull CreateClassesChangeGeneratorFactory changeFactory,
                                             @Nonnull LinearizationManager linearizationManager,
                                             @Nonnull PostcoordinationManager postcoordinationManager) {
        super(accessManager, applyChanges);
        this.changeGeneratorFactory = checkNotNull(changeFactory);
        this.linearizationManager = linearizationManager;
        this.postcoordinationManager = postcoordinationManager;
    }

    @Nonnull
    @Override
    public Class<CreateClassesFromApiAction> getActionClass() {
        return CreateClassesFromApiAction.class;
    }

    @Nonnull
    @Override
    protected List<BuiltInAction> getRequiredExecutableBuiltInActions(CreateClassesFromApiAction action) {
        return asList(CREATE_CLASS, EDIT_ONTOLOGY);
    }


    @Override
    protected ChangeListGenerator<Set<OWLClass>> getChangeListGenerator(CreateClassesFromApiAction action, ExecutionContext executionContext) {
        var owlClassParents = action.parents().stream()
                .map(DataFactory::getOWLClass)
                .collect(ImmutableSet.toImmutableSet());
        var language = action.langTag() != null ? action.langTag() : "en";
        return changeGeneratorFactory.create(action.sourceText(),
                language,
                owlClassParents,
                action.changeRequestId());
    }

    @Override
    protected CreateClassesFromApiResult createActionResult(ChangeApplicationResult<Set<OWLClass>> changeApplicationResult,
                                                            CreateClassesFromApiAction action,
                                                            ExecutionContext executionContext) {

        Set<OWLClass> classes = changeApplicationResult.getSubject();
        classes.forEach(newClass ->
                {
                    try {
                        linearizationManager.mergeLinearizationsFromParents(
                                newClass.getIRI(),
                                action.parents().stream().map(IRI::create).collect(Collectors.toSet()),
                                action.projectId(),
                                executionContext
                        ).get();
                    } catch (InterruptedException | ExecutionException e) {
                        logger.error("MergeLinearizationsError: " + e);
                    }
                    try {
                        postcoordinationManager.createPostcoordinationFromParent(
                                newClass.getIRI(),
                                IRI.create(action.parents().stream().findFirst().get()),
                                action.projectId(),
                                executionContext
                        ).get();
                    } catch (InterruptedException | ExecutionException e) {
                        logger.error("CreatePostcoordinationError: " + e);
                    }
                }
        );
        return new CreateClassesFromApiResult(
                action.changeRequestId(),
                action.projectId(),
                classes.stream().map(OWLClass::toStringID).collect(toImmutableSet())
        );
    }
}
