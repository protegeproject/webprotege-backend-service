package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetNamedIndividualFrame")
public abstract class GetNamedIndividualFrameResult implements Result, GetObjectResult<NamedIndividualFrame> {

    @JsonCreator
    public static GetNamedIndividualFrameResult create(@JsonProperty("frame") NamedIndividualFrame frame) {
        return new AutoValue_GetNamedIndividualFrameResult(frame);
    }

    public abstract NamedIndividualFrame getFrame();

    @JsonIgnore
    @Override
    public NamedIndividualFrame getObject() {
        return getFrame();
    }
}
