package edu.stanford.bmir.protege.web.server.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class OWLOntologyIDSerializer extends StdSerializer<OWLOntologyID> {

    @Inject
    public OWLOntologyIDSerializer() {
        super(OWLOntologyID.class);
    }

    @Override
    public void serialize(OWLOntologyID value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        Optional<IRI> ontologyIri = value.getOntologyIRI();
        if(ontologyIri.isPresent()) {
            gen.writeStringField("ontologyIri", ontologyIri.get().toString());
        }
        Optional<IRI> versionIri = value.getVersionIRI();
        if(versionIri.isPresent()) {
            gen.writeStringField("versionIri", versionIri.get().toString());
        }
        gen.writeEndObject();
    }
}
