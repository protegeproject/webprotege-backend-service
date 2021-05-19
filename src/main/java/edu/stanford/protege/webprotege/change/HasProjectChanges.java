package edu.stanford.protege.webprotege.change;

import edu.stanford.protege.webprotege.pagination.Page;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
public interface HasProjectChanges {

    @Nonnull
    Page<ProjectChange> getProjectChanges();
}
