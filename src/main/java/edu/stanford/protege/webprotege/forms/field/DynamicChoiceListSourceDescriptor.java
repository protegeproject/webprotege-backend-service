package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.criteria.CompositeRootCriteria;
import edu.stanford.protege.webprotege.criteria.MultiMatchType;
import edu.stanford.protege.webprotege.criteria.RootCriteria;
import edu.stanford.protege.webprotege.forms.PropertyNames;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-11
 */
@AutoValue

@JsonTypeName(DynamicChoiceListSourceDescriptor.TYPE)
public abstract class DynamicChoiceListSourceDescriptor implements ChoiceListSourceDescriptor {

    public static final String TYPE = "Dynamic";

    @JsonCreator
    public static DynamicChoiceListSourceDescriptor get(@JsonProperty(PropertyNames.CRITERIA) @Nonnull RootCriteria criteria) {
        if (criteria instanceof CompositeRootCriteria) {
            return new AutoValue_DynamicChoiceListSourceDescriptor((CompositeRootCriteria) criteria);
        }
        else {
            CompositeRootCriteria wrapped = CompositeRootCriteria.get(ImmutableList.of(criteria), MultiMatchType.ALL);
            return new AutoValue_DynamicChoiceListSourceDescriptor(wrapped);
        }
    }

    @JsonProperty(PropertyNames.CRITERIA)
    @Nonnull
    public abstract CompositeRootCriteria getCriteria();

}
