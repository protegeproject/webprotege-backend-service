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
import edu.stanford.protege.webprotege.project.ProjectDetailsManager;
import org.semanticweb.owlapi.model.*;
import org.slf4j.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ExecutionException;

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

    @Nonnull
    private final ProjectDetailsManager projectDetailsManager;


    @Inject
    public CreateClassesFromApiActionHandler(@Nonnull AccessManager accessManager,

                                             @Nonnull HasApplyChanges applyChanges,
                                             @Nonnull CreateClassesChangeGeneratorFactory changeFactory,
                                             @Nonnull LinearizationManager linearizationManager,
                                             @Nonnull PostcoordinationManager postcoordinationManager, @Nonnull ProjectDetailsManager projectDetailsManager) {
        super(accessManager, applyChanges);
        this.changeGeneratorFactory = checkNotNull(changeFactory);
        this.linearizationManager = linearizationManager;
        this.postcoordinationManager = postcoordinationManager;
        this.projectDetailsManager = projectDetailsManager;
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
        var owlClassParent = DataFactory.getOWLClass(action.parent());
        var language = action.langTag();
        if (language == null || language.isEmpty()) {
            language = projectDetailsManager.getProjectSettings(action.projectId()).getDefaultLanguage().getLang();
        }
        return changeGeneratorFactory.create(action.sourceText(),
                language,
                ImmutableSet.of(owlClassParent),
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
                        linearizationManager.createLinearizationFromParent(
                                newClass.getIRI(),
                                IRI.create(action.parent()),
                                action.projectId(),
                                executionContext
                        ).get();
                    } catch (InterruptedException | ExecutionException e) {
                        logger.error("CreateLinearizationsError: " + e);
                    }
                    try {
                        postcoordinationManager.createPostcoordinationFromParent(
                                newClass.getIRI(),
                                IRI.create(action.parent()),
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
