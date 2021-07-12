package edu.stanford.protege.webprotege.access;

import edu.stanford.protege.webprotege.project.ProjectId;
import edu.stanford.protege.webprotege.user.UserId;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-07
 */
public interface RoleAssignmentRepository extends MongoRepository<RoleAssignment, String> {

    void deleteByUserNameAndProjectId(String userName,
                                      ProjectId projectId);
}
