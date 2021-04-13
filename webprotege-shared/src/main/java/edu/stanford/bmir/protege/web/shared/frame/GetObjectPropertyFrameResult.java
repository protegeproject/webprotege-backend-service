package edu.stanford.bmir.protege.web.shared.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetObjectPropertyFrame")
public abstract class GetObjectPropertyFrameResult implements GetObjectResult<ObjectPropertyFrame> {

    @JsonCreator
    public static GetObjectPropertyFrameResult create(@JsonProperty("frame") ObjectPropertyFrame frame) {
        return new AutoValue_GetObjectPropertyFrameResult(frame);
    }

    public abstract ObjectPropertyFrame getFrame();

    @JsonIgnore
    @Override
    public ObjectPropertyFrame getObject() {
        return getFrame();
    }
}
