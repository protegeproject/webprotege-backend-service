package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import edu.stanford.protege.webprotege.common.AnnotationAssertionDictionaryLanguage;
import edu.stanford.protege.webprotege.common.DictionaryLanguage;
import edu.stanford.protege.webprotege.shortform.MultiLingualDictionary;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.util.List;
import java.util.Optional;

public class Json2Entity {

    public static final String IRI_KEY = "@id";

    public static final String TYPE_KEY = "@type";

    private final OWLDataFactory dataFactory;

    private final MultiLingualDictionary dictionaryManager;

    public Json2Entity(OWLDataFactory dataFactory, MultiLingualDictionary dictionaryManager) {
        this.dataFactory = dataFactory;
        this.dictionaryManager = dictionaryManager;
    }

    public Optional<OWLEntity> convert(JsonNode node) {
        if (node.isObject()) {
            var iriNode = node.get(IRI_KEY);
            var typeNode = node.get(TYPE_KEY);
            var entityType = EntityType.values()
                    .stream()
                            .filter(e -> e.getName().equals(typeNode.asText()))
                                    .findFirst();
            var iriString = iriNode.asText();
            return entityType.map(t -> dataFactory.getOWLEntity(t, IRI.create(iriString)));
        }
        else {
            // Look up entity based on @json label.
            var entities = dictionaryManager.getEntities(node.asText(),
                    getJsonDictionaryLanguage());
            return entities.sorted()
                    .findFirst();
        }
    }

    private static @NotNull List<DictionaryLanguage> getJsonDictionaryLanguage() {
        return List.of(AnnotationAssertionDictionaryLanguage.get(OWLRDFVocabulary.RDFS_LABEL.getIRI(), "json"));
    }
}
