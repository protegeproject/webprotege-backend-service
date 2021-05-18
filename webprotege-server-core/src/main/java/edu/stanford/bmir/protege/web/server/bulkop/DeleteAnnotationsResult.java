package edu.stanford.bmir.protege.web.server.bulkop;

import com.google.auto.value.AutoValue;

import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.event.HasEventList;
import edu.stanford.bmir.protege.web.server.event.ProjectEvent;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 2018
 */
@AutoValue

public abstract class DeleteAnnotationsResult implements Result, HasEventList<ProjectEvent<?>> {

}
