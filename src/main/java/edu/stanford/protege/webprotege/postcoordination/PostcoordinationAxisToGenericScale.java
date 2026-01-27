package edu.stanford.protege.webprotege.postcoordination;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PostcoordinationAxisToGenericScale {

    private final String postcoordinationAxis;
    private final String genericPostcoordinationScaleTopClass;

    private final String allowMultiValue;

    public final static String AXIS_TO_GENERIC_SCALE = "PostcoordinationAxisToGenericScale";

    @JsonCreator
    public PostcoordinationAxisToGenericScale(@JsonProperty("postcoordinationAxis") String postcoordinationAxis,
                                              @JsonProperty("genericPostcoordinationScaleTopClass") String genericPostcoordinationScaleTopClass,
                                              @JsonProperty("allowMultiValue") String allowMultiValue) {
        this.postcoordinationAxis = postcoordinationAxis;
        this.genericPostcoordinationScaleTopClass = genericPostcoordinationScaleTopClass;
        this.allowMultiValue = allowMultiValue;
    }


    @JsonProperty("postcoordinationAxis")
    public String getPostcoordinationAxis() {
        return postcoordinationAxis;
    }

    @JsonProperty("genericPostcoordinationScaleTopClass")
    public String getGenericPostcoordinationScaleTopClass() {
        return genericPostcoordinationScaleTopClass;
    }

    @JsonProperty("allowMultiValue")
    public String getAllowMultiValue() {
        return allowMultiValue;
    }
}
