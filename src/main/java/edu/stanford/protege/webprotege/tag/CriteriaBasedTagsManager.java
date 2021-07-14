package edu.stanford.protege.webprotege.tag;

import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.match.MatchingEngine;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Objects;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2018
 */
@ProjectSingleton
public class CriteriaBasedTagsManager {

    @Nonnull
    private final TagRepository tagRepository;

    @Nonnull
    private final MatchingEngine matchingEngine;

    @Nonnull
    private final ProjectId projectId;

    @Inject
    public CriteriaBasedTagsManager(@Nonnull TagRepository tagRepository,
                                    @Nonnull MatchingEngine matchingEngine, @Nonnull ProjectId projectId) {
        this.tagRepository = checkNotNull(tagRepository);
        this.matchingEngine = checkNotNull(matchingEngine);
        this.projectId = projectId;
    }

    public Stream<TagId> getTagsForEntity(@Nonnull OWLEntity entity) {
        return tagRepository.findTags(projectId)
                     .stream()
                     .map(tag -> {
                         if(matchingEngine.matchesAny(entity, tag.getCriteria())) {
                             return tag;
                         }
                         else {
                             return null;
                         }
                     })
                     .filter(Objects::nonNull)
                     .map(Tag::getTagId);
    }

    public Stream<OWLEntity> getTaggedEntities(@Nonnull TagId tagId) {
        return tagRepository.findTagByTagId(tagId)
                     .map(tag -> matchingEngine.matchAny(tag.getCriteria()))
                     .orElse(Stream.empty());
    }
}
