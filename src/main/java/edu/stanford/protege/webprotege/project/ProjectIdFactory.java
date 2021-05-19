package edu.stanford.protege.webprotege.project;

import java.util.UUID;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/04/2013
 */
public class ProjectIdFactory {

    public static ProjectId getFreshProjectId() {
        UUID uuid = UUID.randomUUID();
        return ProjectId.get(uuid.toString());
    }

}
