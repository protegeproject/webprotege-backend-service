package edu.stanford.protege.webprotege.crud;

import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.dispatch.ProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.NullValidator;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class GetEntityCrudKitsActionHandler implements ProjectActionHandler<GetEntityCrudKitsAction, GetEntityCrudKitsResult> {

    @Nonnull
    private final EntityCrudKitRegistry entityCrudKitRegistry;

    @Nonnull
    private final ProjectEntityCrudKitHandlerCache crudKitHandlerCache;

    @Inject
    public GetEntityCrudKitsActionHandler(@Nonnull EntityCrudKitRegistry entityCrudKitRegistry,
                                          @Nonnull ProjectEntityCrudKitHandlerCache crudKitHandlerCache) {
        this.entityCrudKitRegistry = checkNotNull(entityCrudKitRegistry);
        this.crudKitHandlerCache = checkNotNull(crudKitHandlerCache);
    }

    @Nonnull
    @Override
    public Class<GetEntityCrudKitsAction> getActionClass() {
        return GetEntityCrudKitsAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetEntityCrudKitsAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetEntityCrudKitsResult execute(@Nonnull GetEntityCrudKitsAction action, @Nonnull ExecutionContext executionContext) {
        var currentSettings = crudKitHandlerCache.getHandler().getSettings();
        var kits = entityCrudKitRegistry.getKits();
        return new GetEntityCrudKitsResult(new ArrayList<>(kits), currentSettings);
    }
}

