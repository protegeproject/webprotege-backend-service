package edu.stanford.bmir.protege.web.server.form;

import com.google.auto.value.AutoValue;

import edu.stanford.bmir.protege.web.server.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-16
 */
@AutoValue

public abstract class SetEntityFormDescriptorResult implements Result {

    public static SetEntityFormDescriptorResult get() {
        return new AutoValue_SetEntityFormDescriptorResult();
    }
}
