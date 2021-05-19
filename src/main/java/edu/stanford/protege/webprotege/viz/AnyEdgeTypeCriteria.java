package edu.stanford.protege.webprotege.viz;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-06
 */
@AutoValue

@JsonTypeName("AnyEdgeType")
public class AnyEdgeTypeCriteria {

    public static AnyEdgeTypeCriteria get() {
        return new AutoValue_AnyEdgeTypeCriteria();
    }
}
