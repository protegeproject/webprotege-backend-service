package edu.stanford.protege.webprotege;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.persistence.ProjectId2StringConverter;
import edu.stanford.protege.webprotege.persistence.String2ProjectIdConverter;
import edu.stanford.protege.webprotege.persistence.String2UserIdConverter;
import edu.stanford.protege.webprotege.persistence.UserId2StringConverter;
import org.bson.Document;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions.MongoConverterConfigurationAdapter;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-09
 */
@Configuration
public class WebProtegeMongoConfiguration extends AbstractMongoClientConfiguration {

    private final ObjectMapper objectMapper;

    public WebProtegeMongoConfiguration(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Value("${webprotege.database.name}")
    private String databaseName;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    protected void configureConverters(MongoConverterConfigurationAdapter conf) {
        conf.registerConverter(new StringToIriConverter());
        conf.registerConverter(new IriToStringConverter());
        conf.registerConverter(new OwlEntityToDocumentConverter());
        conf.registerConverter(new DocumentToOwlEntityConverter());
        conf.registerConverter(new ProjectId2StringConverter());
        conf.registerConverter(new String2ProjectIdConverter());
        conf.registerConverter(new UserId2StringConverter());
        conf.registerConverter(new String2UserIdConverter());
//        register(ThreadId.class, conf);
//        register(TagId.class, conf);
//        register(ActionId.class, conf);
//        register(CommentId.class, conf);
//        register(FormId.class, conf);
//        register(FormFieldId.class, conf);
//        register(GridColumnId.class, conf);
//        register(PersonId.class, conf);
    }

    public static class StringToIriConverter implements Converter<String, IRI> {

        @Override
        public IRI convert(String s) {
            return IRI.create(s);
        }
    }

    public static class IriToStringConverter implements Converter<IRI, String> {

        @Override
        public String convert(IRI iri) {
            return iri.toString();
        }
    }

    public static class OwlEntityToDocumentConverter implements Converter<OWLEntity, Document> {

        @Override
        public Document convert(OWLEntity entity) {
            return new Document().append("iri", entity.getIRI().toString())
                    .append("type", entity.getEntityType().toString());
        }
    }

    public static class DocumentToOwlEntityConverter implements Converter<Document, OWLEntity> {

        private final OWLDataFactory dataFactory = new OWLDataFactoryImpl();

        @Override
        public OWLEntity convert(Document document) {
            var iriString = document.getString("iri");
            var entityTypeString = document.getString("type");
            for(EntityType entityType : EntityType.values()) {
                if(entityTypeString.equals(entityType.toString())) {
                    return dataFactory.getOWLEntity(entityType, IRI.create(iriString));
                }
            }
            return null;
        }
    }
}
