package edu.stanford.bmir.protege.web.shared.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.mansyntax.AutoCompletionResult;

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
