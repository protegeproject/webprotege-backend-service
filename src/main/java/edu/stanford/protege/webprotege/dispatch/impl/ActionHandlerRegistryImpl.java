package edu.stanford.protege.webprotege.dispatch.impl;

import edu.stanford.protege.webprotege.common.Request;
import edu.stanford.protege.webprotege.common.Response;
import edu.stanford.protege.webprotege.dispatch.*;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 */
public abstract class ActionHandlerRegistryImpl implements ActionHandlerRegistry {

    // NOT a concurrent map.  This is only written to in the constructor. At runtime it's essentially immutable and the
    // basic maps are safe for multiple readers
    private final Map<Class<?>, ActionHandler<?, ?>> registry = new HashMap<>();

    public ActionHandlerRegistryImpl(Set<? extends ActionHandler> handlers) {
        for(ActionHandler<?, ?> actionHandler : handlers) {
            register(actionHandler);
        }
    }


    private  <A extends Request<R>, R extends Response> void register(ActionHandler<A, R> handler) {
        registry.put(handler.getActionClass(), handler);
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    public <A extends Request<R>, R extends Response> ActionHandler<A, R> getActionHandler(A action) {
        checkNotNull(action, "action must not be null");
        ActionHandler<A, R> handler = (ActionHandler<A, R>) registry.get(action.getClass());
        if(handler == null) {
            handler = (ActionHandler<A, R>) registry.get(action.getClass().getSuperclass());
            if (handler == null) {
                throw new ActionHandlerNotFoundException(action);
            }
        }
        return handler;
    }
}
