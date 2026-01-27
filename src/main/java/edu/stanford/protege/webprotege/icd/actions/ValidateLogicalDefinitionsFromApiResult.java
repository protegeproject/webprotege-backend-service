package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.dispatch.Result;

import javax.annotation.Nonnull;
import java.util.List;

public record ValidateLogicalDefinitionsFromApiResult(
        @JsonProperty("messages") @Nonnull List<String> messages
) implements Result {
    
    public static ValidateLogicalDefinitionsFromApiResult create(@Nonnull List<String> messages) {
        return new ValidateLogicalDefinitionsFromApiResult(messages);
    }
}
