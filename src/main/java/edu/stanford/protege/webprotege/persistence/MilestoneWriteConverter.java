package edu.stanford.protege.webprotege.persistence;

import edu.stanford.protege.webprotege.issues.Milestone;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Aug 16
 */
public class MilestoneWriteConverter implements Converter<Milestone, String> {

    @Override
    public String convert(Milestone milestone) {
        return milestone.getLabel();
    }
}
