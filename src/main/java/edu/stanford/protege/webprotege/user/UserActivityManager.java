package edu.stanford.protege.webprotege.user;

import edu.stanford.protege.webprotege.inject.ApplicationSingleton;
import edu.stanford.protege.webprotege.persistence.Repository;
import edu.stanford.protege.webprotege.project.ProjectId;
import edu.stanford.protege.webprotege.project.RecentProjectRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static edu.stanford.protege.webprotege.user.UserActivityRecord.*;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Mar 2017
 */
@ApplicationSingleton
public class UserActivityManager implements Repository {

    private static final Logger logger = LoggerFactory.getLogger(UserActivityManager.class);

    private MongoOperations mongoTemplate;

    @Inject
    public UserActivityManager(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public void ensureIndexes() {
    }

    public void save(UserActivityRecord record) {
        if(record.getUserId().isGuest()) {
            return;
        }
        mongoTemplate.save(record);
    }

    public Optional<UserActivityRecord> getUserActivityRecord(UserId userId) {
        if(userId.isGuest()) {
            return Optional.empty();
        }
        UserActivityRecord record = mongoTemplate.findOne(query(where(USER_ID).is(userId)), UserActivityRecord.class);
        return Optional.ofNullable(record);
    }

    private UserActivityRecord getByUserId(UserId userId) {
        UserActivityRecord record = mongoTemplate.findOne(query(where(USER_ID).is(userId)), UserActivityRecord.class);
        if (record == null) {
            mongoTemplate.save(record = UserActivityRecord.get(userId));
        }
        return record;
    }

    public void setLastLogin(@Nonnull UserId userId, long lastLogin) {
        if(userId.isGuest()) {
            return;
        }
        mongoTemplate.updateFirst(query(where(USER_ID).is(userId)),
                                  update(LAST_LOGIN, new Date(lastLogin)),
                                  UserActivityRecord.class);
    }

    public void setLastLogout(@Nonnull UserId userId, long lastLogout) {
        if(userId.isGuest()) {
            return;
        }
        mongoTemplate.updateFirst(query(where(USER_ID).is(userId)),
                                  update(LAST_LOGOUT, new Date(lastLogout)),
                                  UserActivityRecord.class);

    }

    public void addRecentProject(@Nonnull UserId userId, @Nonnull ProjectId projectId, long timestamp) {
        if(userId.isGuest()) {
            return;
        }
        UserActivityRecord record = getByUserId(userId);
        List<RecentProjectRecord> recentProjects = record.getRecentProjects().stream()
                                                         .filter(recentProject -> !recentProject.getProjectId()
                                                                                                .equals(projectId))
                                                         .sorted()
                                                         .collect(toList());
        recentProjects.add(0, new RecentProjectRecord(projectId, timestamp));
        UserActivityRecord replacement = new UserActivityRecord(
                record.getUserId(),
                record.getLastLogin(),
                record.getLastLogout(),
                recentProjects
        );
        save(replacement);
    }
}
