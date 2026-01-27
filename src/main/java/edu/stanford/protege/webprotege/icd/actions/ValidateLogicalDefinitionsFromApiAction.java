package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;

import javax.annotation.Nonnull;
import java.util.List;

@JsonTypeName(ValidateLogicalDefinitionsFromApiAction.CHANNEL)
public record ValidateLogicalDefinitionsFromApiAction(
        @JsonProperty("projectId") @Nonnull ProjectId projectId,
        @JsonProperty("entityIri") @Nonnull String entityIri,
        @JsonProperty("logicalDefinitions") @Nonnull List<LogicalDefinition> logicalDefinitions,
        @JsonProperty("necessaryConditions") @Nonnull List<Relationship> necessaryConditions
) implements ProjectAction<ValidateLogicalDefinitionsFromApiResult> {
    
    public static final String CHANNEL = "webprotege.icd.ValidateLogicalDefinitionsFromApi";

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    public record LogicalDefinition(
            @JsonProperty("logicalDefinitionSuperclass") @Nonnull String logicalDefinitionSuperclass,
            @JsonProperty("relationships") @Nonnull List<Relationship> relationships
    ) {}

    public record Relationship(
            @JsonProperty("axis") @Nonnull String axis,
            @JsonProperty("filler") @Nonnull String filler
    ) {}
}
