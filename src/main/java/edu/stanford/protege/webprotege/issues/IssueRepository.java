package edu.stanford.protege.webprotege.issues;

import edu.stanford.protege.webprotege.common.ProjectId;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Jul 16
 */
public interface IssueRepository {

    Issue save(Issue s);

    Optional<Issue> findByProjectIdAndNumber(ProjectId projectId, long issueNumber);

    Optional<Issue> findOneByProjectIdOrderByNumberDesc(ProjectId projectId);

    Stream<Issue> findByProjectId(ProjectId projectId);

    long countByProjectId(ProjectId projectId);
}
