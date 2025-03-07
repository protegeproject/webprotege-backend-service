package edu.stanford.protege.webprotege.entity;

import org.semanticweb.owlapi.model.IRI;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "IcatxEntityTypeConfigurations")
public record IcatxEntityTypeConfiguration(IRI topLevelIri, String excludesEntityType, String icatxEntityType) {
}
