package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/01/2013
 * <p>
 *     A high level interface for frame objects.  All frames have some kind of subject.
 * </p>
 */
@JsonSubTypes(@JsonSubTypes.Type(EntityFrame.class))
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface Frame<S> {

    /**
     * Gets the subject of this frame.
     * @return The subject. Not {@code null}.
     */
    S getSubject();

    @Nonnull
    PlainEntityFrame toPlainFrame();
}
