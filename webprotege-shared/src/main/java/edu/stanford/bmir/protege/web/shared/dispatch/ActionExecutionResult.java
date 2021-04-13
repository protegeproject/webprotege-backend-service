package edu.stanford.bmir.protege.web.shared.dispatch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Oct 2018
 */
@GwtCompatible(serializable = true)
@AutoValue
public abstract class ActionExecutionResult implements IsSerializable {

    @JsonCreator
    private static ActionExecutionResult create(@Nullable @JsonProperty("result") DispatchServiceResultContainer result,
                                    @Nullable @JsonProperty("permissionDeniedException") PermissionDeniedException permissionDeniedException,
                                    @Nullable @JsonProperty("actionExecutionException") ActionExecutionException actionExecutionException) {
        return new AutoValue_ActionExecutionResult(result, permissionDeniedException, actionExecutionException);
    }

    /**
     * Get an {@link ActionExecutionResult} for the results of an action.  The results is
     * wrapped in a {@link DispatchServiceResultContainer}.
     */
    public static ActionExecutionResult get(DispatchServiceResultContainer result) {
        return create(result, null, null);
    }

    /**
     * Get an {@link ActionExecutionResult} for a permission denied exception
     * @param permissionDeniedException The exception.
     */
    public static ActionExecutionResult get(PermissionDeniedException permissionDeniedException) {
        return create(null, permissionDeniedException, null);
    }

    /**
     * Get an {@link ActionExecutionResult} for an {@link ActionExecutionException}.
     */
    public static ActionExecutionResult get(ActionExecutionException actionExecutionException) {
        return create(null, null, actionExecutionException);
    }

    @Nonnull
    public Optional<DispatchServiceResultContainer> getResult() {
        return Optional.ofNullable(getResultInternal());
    }

    @Nullable
    @JsonIgnore
    protected abstract DispatchServiceResultContainer getResultInternal();

    @Nonnull
    public Optional<PermissionDeniedException> getPermissionDeniedException() {
        return Optional.ofNullable(getPermissionDeniedExceptionInternal());
    }

    @Nullable
    public abstract PermissionDeniedException getPermissionDeniedExceptionInternal();

    @Nonnull
    @JsonIgnore
    public Optional<ActionExecutionException> getActionExecutionException() {
        return Optional.ofNullable(getActionExecutionExceptionInternal());
    }

    @Nullable
    public abstract ActionExecutionException getActionExecutionExceptionInternal();
}
