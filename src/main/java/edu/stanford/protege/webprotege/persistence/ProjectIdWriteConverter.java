package edu.stanford.protege.webprotege.persistence;

import edu.stanford.protege.webprotege.common.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/20/13
 * <p>
 *     An implementation of {@link Converter} that writes a {@link ProjectId} to a {@link String}.
 * </p>
 */
public class ProjectIdWriteConverter implements Converter<ProjectId, String> {

    public String convert(ProjectId projectId) {
        return projectId.id();
    }
}
