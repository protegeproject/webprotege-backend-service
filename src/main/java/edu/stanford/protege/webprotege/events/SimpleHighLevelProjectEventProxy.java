package edu.stanford.protege.webprotege.events;

import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.common.ProjectEvent;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-05
 */
@AutoValue
public abstract class SimpleHighLevelProjectEventProxy implements HighLevelProjectEventProxy {

    @Nonnull
    public static SimpleHighLevelProjectEventProxy wrap(@Nonnull ProjectEvent newWrappedEvent) {
        return new AutoValue_SimpleHighLevelProjectEventProxy(newWrappedEvent);
    }

    @Nonnull
    abstract ProjectEvent getWrappedEvent();

    @Nonnull
    @Override
    public ProjectEvent asProjectEvent() {
        return getWrappedEvent();
    }
}
