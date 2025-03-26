package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.IRI;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "IcatxEntityTypeConfigurations")
public record IcatxEntityTypeConfiguration(IRI topLevelIri, ProjectId projectId, String excludesEntityType, String icatxEntityType) {
}
