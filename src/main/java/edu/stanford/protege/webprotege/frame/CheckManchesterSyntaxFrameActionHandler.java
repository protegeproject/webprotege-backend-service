package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInCapability;
import edu.stanford.protege.webprotege.change.ChangeGenerationContext;
import edu.stanford.protege.webprotege.change.OntologyChangeList;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.mansyntax.ManchesterSyntaxChangeGenerator;
import edu.stanford.protege.webprotege.mansyntax.ManchesterSyntaxChangeGeneratorFactory;
import edu.stanford.protege.webprotege.mansyntax.ManchesterSyntaxFrameParser;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jakarta.inject.Inject;

import static edu.stanford.protege.webprotege.frame.ManchesterSyntaxFrameParseResult.CHANGED;
import static edu.stanford.protege.webprotege.frame.ManchesterSyntaxFrameParseResult.UNCHANGED;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class CheckManchesterSyntaxFrameActionHandler extends AbstractProjectActionHandler<LocalCheckManchesterSyntaxFrameAction, LocalCheckManchesterSyntaxFrameResult> {

    @Nonnull
    private final ManchesterSyntaxChangeGeneratorFactory factory;

    @Nonnull
    private final RenderingManager renderer;

    @Inject
    public CheckManchesterSyntaxFrameActionHandler(@Nonnull AccessManager accessManager,
                                                   @Nonnull ManchesterSyntaxChangeGeneratorFactory factory,
                                                   @Nonnull RenderingManager renderer) {
        super(accessManager);
        this.factory = factory;
        this.renderer = renderer;
    }

    @Nullable
    @Override
    protected BuiltInCapability getRequiredExecutableBuiltInAction(LocalCheckManchesterSyntaxFrameAction action) {
        return BuiltInCapability.VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public LocalCheckManchesterSyntaxFrameResult execute(@Nonnull LocalCheckManchesterSyntaxFrameAction action,
                                                    @Nonnull ExecutionContext executionContext) {

        ManchesterSyntaxChangeGenerator changeGenerator = factory.create(
                renderer.getRendering(action.subject()),
                action.from(),
                action.to(),
                "",
                CheckManchesterSyntaxFrameAction.create(action.projectId(), action.subject(), action.from(), action.to(), action.freshEntities()));
        try {
            OntologyChangeList<?> changeList = changeGenerator.generateChanges(new ChangeGenerationContext(executionContext.userId()));
            if (changeList.getChanges().isEmpty()) {
                return LocalCheckManchesterSyntaxFrameResult.create(UNCHANGED);
            }
            else {
                return LocalCheckManchesterSyntaxFrameResult.create(CHANGED);
            }
        } catch (ParserException e) {
            return LocalCheckManchesterSyntaxFrameResult.create(ManchesterSyntaxFrameParser.getParseError(e));
        }
    }

    @Nonnull
    @Override
    public Class<LocalCheckManchesterSyntaxFrameAction> getActionClass() {
        return LocalCheckManchesterSyntaxFrameAction.class;
    }
}

