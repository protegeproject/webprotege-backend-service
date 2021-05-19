package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.mansyntax.AutoCompletionResult;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/03/2014
 */
@AutoValue

@JsonTypeName("GetManchesterSyntaxFrameCompletions")
public abstract class GetManchesterSyntaxFrameCompletionsResult implements Result {

    @JsonCreator
    public static GetManchesterSyntaxFrameCompletionsResult create(@JsonProperty("autoCompletionResult") AutoCompletionResult autoCompletionResult) {
        return new AutoValue_GetManchesterSyntaxFrameCompletionsResult(autoCompletionResult);
    }

    public abstract AutoCompletionResult getAutoCompletionResult();
}
