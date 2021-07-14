package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.entity.OWLEntityData;

import javax.annotation.Nonnull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
@AutoValue

@JsonTypeName("GetManchesterSyntaxFrame")
public abstract class GetManchesterSyntaxFrameResult implements Result {

    private static final String SUBJECT = "subject";

    private static final String RENDERING = "rendering";

    @JsonCreator
    @Nonnull
    public static GetManchesterSyntaxFrameResult create(@JsonProperty(SUBJECT) @Nonnull OWLEntityData frameSubject,
                                                        @JsonProperty(RENDERING) @Nonnull String frameRendering) {
        return new AutoValue_GetManchesterSyntaxFrameResult(frameSubject,
                                                            frameRendering);
    }

    @JsonProperty(SUBJECT)
    @Nonnull
    public abstract OWLEntityData getFrameSubject();

    @JsonProperty(RENDERING)
    @Nonnull
    public abstract String getFrameManchesterSyntax();
}
