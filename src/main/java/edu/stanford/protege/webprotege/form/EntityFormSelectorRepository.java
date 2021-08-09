package edu.stanford.protege.webprotege.form;

import edu.stanford.protege.webprotege.persistence.Repository;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-08
 */
public interface EntityFormSelectorRepository extends Repository {

    void save(EntityFormSelector entityFormSelector);

    Stream<EntityFormSelector> findFormSelectors(@Nonnull ProjectId projectId);
}
