package edu.stanford.bmir.protege.web.shared.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.gwtcodemirror.client.AutoCompletionResult;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/03/2014
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetManchesterSyntaxFrameCompletions")
public abstract class GetManchesterSyntaxFrameCompletionsResult implements Result {

    @JsonCreator
    public static GetManchesterSyntaxFrameCompletionsResult create(@JsonProperty("autoCompletionResult") AutoCompletionResult autoCompletionResult) {
        return new AutoValue_GetManchesterSyntaxFrameCompletionsResult(autoCompletionResult);
    }

    public abstract AutoCompletionResult getAutoCompletionResult();
}
