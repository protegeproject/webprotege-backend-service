package edu.stanford.protege.webprotege.persistence;

import edu.stanford.protege.webprotege.common.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-09
 */
public class String2ProjectIdConverter extends String2ValueObjectConverter<ProjectId> {

    public String2ProjectIdConverter() {
        super(ProjectId.class);
    }
}
