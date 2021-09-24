package edu.stanford.protege.webprotege.crud;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public interface EntityCrudKitPlugin<H extends EntityCrudKitHandler<S, C>, S extends EntityCrudKitSuffixSettings, C extends ChangeSetEntityCrudSession> {

    EntityCrudKit<S> getEntityCrudKit();

    EntityCrudKitHandler<S, C> getEntityCrudKitHandler();

    EntityCrudKitHandler<S, C> getEntityCrudKitHandler(EntityCrudKitSettings settings);

    S getDefaultSettings();
}
