package edu.stanford.bmir.protege.web.shared.app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("SetApplicationSettings")
public abstract class SetApplicationSettingsAction implements Action<SetApplicationSettingsResult> {

    @JsonCreator
    public static SetApplicationSettingsAction create(@JsonProperty("applicationSettings") @Nonnull ApplicationSettings applicationSettings) {
        return new AutoValue_SetApplicationSettingsAction(applicationSettings);
    }

    public abstract ApplicationSettings getApplicationSettings();
}
