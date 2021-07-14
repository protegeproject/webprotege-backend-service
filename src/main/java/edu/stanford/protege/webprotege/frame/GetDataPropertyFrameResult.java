package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
@AutoValue

@JsonTypeName("GetDataPropertyFrame")
public abstract class GetDataPropertyFrameResult implements Result {

    @JsonCreator
    public static GetDataPropertyFrameResult create(@JsonProperty("frame") DataPropertyFrame frame) {
        return new AutoValue_GetDataPropertyFrameResult(frame);
    }

    public abstract DataPropertyFrame getFrame();
}
