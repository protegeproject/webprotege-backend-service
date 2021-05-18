package edu.stanford.bmir.protege.web.server.match.criteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */
public interface AnnotationCriteriaVisitor<R> {

    @Nonnull
    R visit(@Nonnull AnnotationComponentsCriteria criteria);
}
