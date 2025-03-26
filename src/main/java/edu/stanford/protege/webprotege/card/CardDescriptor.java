package edu.stanford.protege.webprotege.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.authorization.ActionId;
import edu.stanford.protege.webprotege.color.Color;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.criteria.CompositeRootCriteria;

import java.util.Set;

public record CardDescriptor(@JsonProperty("id") CardId cardId,
                             @JsonProperty("label") LanguageMap label,
                             @JsonProperty("color") Color color,
                             @JsonProperty("backgroundColor") Color backgroundColor,
                             @JsonProperty("content") CardContentDescriptor contentDescriptor,
                             @JsonProperty("requiredReadActions") Set<ActionId> requiredReadActions,
                             @JsonProperty("requiredWriteActions") Set<ActionId> requiredWriteActions,
                             @JsonProperty("visibilityCriteria") CompositeRootCriteria visibilityCriteria) {

    public static CardDescriptor create(CardId cardId, LanguageMap label, Color color, Color backgroundColor, CardContentDescriptor contentDescriptor, Set<ActionId> requiredReadActions, Set<ActionId> requiredWriteActions, CompositeRootCriteria visibilityCriteria) {
        return new CardDescriptor(cardId, label, color, backgroundColor, contentDescriptor, requiredReadActions, requiredWriteActions, visibilityCriteria);
    }
}
