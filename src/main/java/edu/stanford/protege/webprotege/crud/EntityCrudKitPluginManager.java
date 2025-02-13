package edu.stanford.protege.webprotege.crud;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
@ProjectSingleton
public class EntityCrudKitPluginManager {

    private final ImmutableList<EntityCrudKitPlugin<?,?,?>> plugins;

    @Inject
    public EntityCrudKitPluginManager(@Nonnull Set<EntityCrudKitPlugin<?,?,?>> plugins) {
        this.plugins = ImmutableList.copyOf(plugins);
    }

    public List<EntityCrudKitPlugin<?,?,?>> getPlugins() {
        return plugins;
    }
}
