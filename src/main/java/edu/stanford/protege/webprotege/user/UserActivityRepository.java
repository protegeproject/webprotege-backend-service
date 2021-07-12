package edu.stanford.protege.webprotege.user;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-07
 */
public interface UserActivityRepository extends MongoRepository<UserActivityRecord, String> {


    UserActivityRecord findByUserId(UserId userId);
}
