package edu.stanford.protege.webprotege.events;

import edu.stanford.protege.webprotege.common.ProjectEvent;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-05
 */
public interface HighLevelProjectEventProxy {

    @Nonnull
    ProjectEvent asProjectEvent();

}
