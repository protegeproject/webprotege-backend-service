package edu.stanford.protege.webprotege.persistence;

import edu.stanford.protege.webprotege.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-09
 */
public class ProjectId2StringConverter extends ValueObject2StringConverter<ProjectId> {

    public ProjectId2StringConverter() {
        super(ProjectId.class);
    }
}
