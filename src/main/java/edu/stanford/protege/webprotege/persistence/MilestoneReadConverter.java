package edu.stanford.protege.webprotege.persistence;

import edu.stanford.protege.webprotege.issues.Milestone;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Aug 16
 */
public class MilestoneReadConverter implements Converter<String, Milestone> {

    @Override
    public Milestone convert(String s) {
        return new Milestone(s);
    }
}
