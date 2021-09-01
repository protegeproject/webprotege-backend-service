package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import edu.stanford.protege.webprotege.criteria.Criteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-16
 */
@JsonSubTypes(
        {
                @JsonSubTypes.Type(EntityFormControlDataMatchCriteria.class),
                @JsonSubTypes.Type(LiteralFormControlDataMatchCriteria.class)
        }
)
public interface PrimitiveFormControlDataMatchCriteria extends Criteria {

        <R> R accept(@Nonnull PrimitiveFormControlDataMatchCriteriaVisitor<R> visitor);

}
