package edu.stanford.protege.webprotege.csv;

import org.semanticweb.owlapi.model.EntityType;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/05/2013
 */
public enum CSVRowImportType {

    CLASS("Class", EntityType.CLASS),

    INDIVIDUAL("Individual", EntityType.NAMED_INDIVIDUAL);

    private final String displayName;

    private final EntityType<?> entityType;

    CSVRowImportType(String displayName, EntityType<?> entityType) {
        this.displayName = displayName;
        this.entityType = entityType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public EntityType<?> getEntityType() {
        return entityType;
    }
}
