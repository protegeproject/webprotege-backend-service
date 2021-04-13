package edu.stanford.bmir.protege.web.shared.obo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("SetOboTermDefinition")
public class SetOboTermDefinitionResult implements Result {

    @JsonCreator
    public static SetOboTermDefinitionResult create() {
        return new AutoValue_SetOboTermDefinitionResult();
    }
}
