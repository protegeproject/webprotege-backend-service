package edu.stanford.protege.webprotege.persistence;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import edu.stanford.protege.webprotege.issues.Mention;
import edu.stanford.protege.webprotege.issues.mention.EntityMention;
import edu.stanford.protege.webprotege.issues.mention.IssueMention;
import edu.stanford.protege.webprotege.issues.mention.RevisionMention;
import edu.stanford.protege.webprotege.issues.mention.UserIdMention;
import edu.stanford.protege.webprotege.common.UserId;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30 Sep 2016
 */
public class MentionReadConverter implements Converter<BasicDBObject, Mention> {

    private final OWLEntityReadConverter entityReadConverter = new OWLEntityReadConverter();

    @Override
    public Mention convert(BasicDBObject dbObject) {
        Object mentionClass = dbObject.get("_class" );
        if("IssueMention".equals(mentionClass)) {
            return new IssueMention(dbObject.getInt("issueNumber"));
        }
        else if("RevisionMention".equals(mentionClass)) {
            return new RevisionMention(dbObject.getLong("revisionNumber"));
        }
        else if("UserIdMention".equals(mentionClass)) {
            return new UserIdMention(UserId.valueOf(dbObject.get("userId").toString()));
        }
        else if("EntityMention".equals(mentionClass)) {
            DBObject entityObject = (DBObject) dbObject.get("entity");
            return new EntityMention(entityReadConverter.convert(entityObject));
        }
        throw new RuntimeException("Unknown class of Mention: " + mentionClass);
    }
}
