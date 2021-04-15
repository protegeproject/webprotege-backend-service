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
@JsonTypeName("GetDataPropertyFrame")
public abstract class GetDataPropertyFrameResult implements GetObjectResult<DataPropertyFrame> {

    @JsonCreator
    public static GetDataPropertyFrameResult create(@JsonProperty("frame") DataPropertyFrame frame) {
        return new AutoValue_GetDataPropertyFrameResult(frame);
    }

    /**
     * Gets the object.
     *
     * @return The object.  Not {@code null}.
     */
    @JsonIgnore
    @Override
    public DataPropertyFrame getObject() {
        return getFrame();
    }

    public abstract DataPropertyFrame getFrame();
}
