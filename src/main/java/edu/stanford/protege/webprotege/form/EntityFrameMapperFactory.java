package edu.stanford.protege.webprotege.form;

import edu.stanford.protege.webprotege.index.ClassAssertionAxiomsByClassIndex;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.match.MatcherFactory;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import edu.stanford.protege.webprotege.frame.PlainEntityFrame;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-26
 */
public class EntityFrameMapperFactory {

    @Nonnull
    private final ClassAssertionAxiomsByClassIndex classAssertionAxiomsIndex;

    @Nonnull
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private RenderingManager renderingManager;

    @Nonnull
    private final MatcherFactory matcherFactory;

    @Inject
    public EntityFrameMapperFactory(@Nonnull ClassAssertionAxiomsByClassIndex classAssertionAxiomsIndex,
                                    @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                    @Nonnull RenderingManager renderingManager,
                                    @Nonnull MatcherFactory matcherFactory) {
        this.classAssertionAxiomsIndex = checkNotNull(classAssertionAxiomsIndex);
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.renderingManager = checkNotNull(renderingManager);
        this.matcherFactory = checkNotNull(matcherFactory);
    }

    public EntityFrameMapper create(@Nonnull PlainEntityFrame entityFrame) {
        return new EntityFrameMapper(entityFrame, projectOntologiesIndex, classAssertionAxiomsIndex, renderingManager,
                                     matcherFactory);
    }
}
