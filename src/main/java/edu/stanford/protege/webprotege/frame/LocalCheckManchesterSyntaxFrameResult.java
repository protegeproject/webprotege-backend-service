package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Response;

import javax.annotation.Nullable;
import java.util.Optional;

@JsonTypeName("CheckManchesterSyntax")
public record LocalCheckManchesterSyntaxFrameResult(@Nullable ManchesterSyntaxFrameParseResult result, @Nullable ManchesterSyntaxFrameParseError error) implements Response {
    public Optional<ManchesterSyntaxFrameParseError> getError() {
        return Optional.ofNullable(this.error);
    }

    public static LocalCheckManchesterSyntaxFrameResult create(ManchesterSyntaxFrameParseResult result) {
        return new LocalCheckManchesterSyntaxFrameResult(result, (ManchesterSyntaxFrameParseError)null);
    }

    public static LocalCheckManchesterSyntaxFrameResult create(ManchesterSyntaxFrameParseError error) {
        return new LocalCheckManchesterSyntaxFrameResult((ManchesterSyntaxFrameParseResult)null, error);
    }
}
