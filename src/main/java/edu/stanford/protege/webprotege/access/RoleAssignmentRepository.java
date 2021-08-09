package edu.stanford.protege.webprotege.access;

import edu.stanford.protege.webprotege.common.ProjectId;
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
