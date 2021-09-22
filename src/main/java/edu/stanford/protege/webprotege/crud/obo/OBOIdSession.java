package edu.stanford.protege.webprotege.crud.obo;

import com.google.common.collect.Sets;
import edu.stanford.protege.webprotege.crud.ChangeSetEntityCrudSession;

import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 15/09/2014
 */
public class OBOIdSession implements ChangeSetEntityCrudSession {

    private final Set<Long> sessionIds = Sets.newHashSet();

    public OBOIdSession() {
    }

    public boolean isSessionId(long id) {
        return sessionIds.contains(id);
    }

    public void addSessionId(long id) {
        sessionIds.add(id);
    }
}
