package edu.stanford.protege.webprotege.issues;

import com.mongodb.MongoClient;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30 Sep 2016
 */
public class IssueStore {

    private final MongoClient client;

    public IssueStore(MongoClient client) {
        this.client = client;
    }

    public int getNextIssueNumber() {
        return 0;
    }

    public Optional<Issue> getIssue(int issueNumber) {
        return Optional.empty();
    }
}
