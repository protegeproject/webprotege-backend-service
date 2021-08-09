package edu.stanford.protege.webprotege.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.match.MatchingEngine;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class EntityFormManager {

    @Nonnull
    private final EntityFormRepository entityFormRepository;

    @Nonnull
    private final EntityFormSelectorRepository entityFormSelectorRepository;

    @Nonnull
    private final MatchingEngine matchingEngine;

    @Inject
    public EntityFormManager(@Nonnull EntityFormRepository entityFormRepository,
                             @Nonnull EntityFormSelectorRepository entityFormSelectorRepository,
                             @Nonnull MatchingEngine matchingEngine) {
        this.entityFormRepository = entityFormRepository;
        this.entityFormSelectorRepository = entityFormSelectorRepository;
        this.matchingEngine = matchingEngine;
    }

    @Nonnull
    public ImmutableList<FormDescriptor> getFormDescriptors(@Nonnull OWLEntity entity,
                                                            @Nonnull ProjectId projectId,
                                                            @Nonnull FormPurpose formPurpose) {
        var formIds = entityFormSelectorRepository.findFormSelectors(projectId)
                                                  .filter(selector -> selector.getPurpose().equals(formPurpose))
                                    .filter(selector -> matchingEngine.matches(entity,
                                                                               selector.getCriteria()))
                                    .map(EntityFormSelector::getFormId)
                                    .collect(toImmutableSet());
        return entityFormRepository.findFormDescriptors(formIds, projectId)
                            .collect(toImmutableList());
    }

}
