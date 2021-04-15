package edu.stanford.bmir.protege.web.shared.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetAnnotationPropertyFrame")
public abstract class GetAnnotationPropertyFrameResult implements Result, GetObjectResult<AnnotationPropertyFrame> {

    @JsonCreator
    public static GetAnnotationPropertyFrameResult create(@JsonProperty("frame") AnnotationPropertyFrame frame) {
        return new AutoValue_GetAnnotationPropertyFrameResult(frame);
    }

    public abstract AnnotationPropertyFrame getFrame();

    @JsonIgnore
    @Override
    public AnnotationPropertyFrame getObject() {
        return getFrame();
    }
}
