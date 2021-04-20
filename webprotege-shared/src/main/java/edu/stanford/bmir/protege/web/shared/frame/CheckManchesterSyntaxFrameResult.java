package edu.stanford.bmir.protege.web.shared.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;


import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
@JsonTypeName("CheckManchesterSyntax")
@AutoValue

public abstract class CheckManchesterSyntaxFrameResult implements Result {

    public abstract ManchesterSyntaxFrameParseResult getResult();

    @JsonIgnore
    @Nullable
    protected abstract ManchesterSyntaxFrameParseError getErrorInternal();

    public Optional<ManchesterSyntaxFrameParseError> getError() {
        return Optional.ofNullable(getErrorInternal());
    }

    public static CheckManchesterSyntaxFrameResult create(ManchesterSyntaxFrameParseResult result) {
        return create(result, null);
    }

    public static CheckManchesterSyntaxFrameResult create(ManchesterSyntaxFrameParseError error) {
        return create(null, error);
    }

    @JsonCreator
    protected static CheckManchesterSyntaxFrameResult create(@JsonProperty("result") ManchesterSyntaxFrameParseResult result,
                                                          @JsonProperty("error") ManchesterSyntaxFrameParseError errorInternal) {
        return new AutoValue_CheckManchesterSyntaxFrameResult(result, errorInternal);
    }
}
