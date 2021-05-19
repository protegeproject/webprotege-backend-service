package edu.stanford.protege.webprotege.persistence;

import edu.stanford.protege.webprotege.project.ProjectId;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.mapping.MappedField;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 */
public class ProjectIdConverter extends TypeSafeConverter<String, ProjectId> implements SimpleValueConverter {

    @Inject
    public ProjectIdConverter() {
        super(ProjectId.class);
    }

    @Override
    public ProjectId decodeObject(String fromDBObject, MappedField optionalExtraInfo) {
        return ProjectId.get(fromDBObject);
    }

    @Override
    public String encodeObject(ProjectId value, MappedField optionalExtraInfo) {
        return value.getId();
    }
}
