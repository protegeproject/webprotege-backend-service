package edu.stanford.protege.webprotege.bulkop;

import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.event.HasEventList;
import edu.stanford.protege.webprotege.event.ProjectEvent;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 2018
 */
@AutoValue

public abstract class DeleteAnnotationsResult implements Result, HasEventList<ProjectEvent<?>> {

}
