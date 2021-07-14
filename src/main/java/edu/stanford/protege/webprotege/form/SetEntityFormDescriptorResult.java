package edu.stanford.protege.webprotege.form;

import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;

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
