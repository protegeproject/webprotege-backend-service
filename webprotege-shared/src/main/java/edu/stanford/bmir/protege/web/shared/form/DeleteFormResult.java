package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-15
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("DeleteForm")
public abstract class DeleteFormResult implements Result {

    @JsonCreator
    public static DeleteFormResult create() {
        return new AutoValue_DeleteFormResult();
    }
}
