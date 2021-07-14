package edu.stanford.protege.webprotege.viz;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.project.HasProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-07
 */
@AutoValue

public abstract class GetProjectEntityGraphDefaultEdgeCriteriaResult implements Result, HasProjectId {

    @Nonnull
    public abstract ImmutableList<EdgeCriteria> getEdgeCriteria();
}
