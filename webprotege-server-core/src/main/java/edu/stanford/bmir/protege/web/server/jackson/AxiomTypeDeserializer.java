package edu.stanford.bmir.protege.web.server.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.semanticweb.owlapi.model.AxiomType;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-08
 */
public class AxiomTypeDeserializer extends StdDeserializer<AxiomType> {

    public AxiomTypeDeserializer() {
        super(AxiomType.class);
    }

    @Override
    public AxiomType deserialize(JsonParser p,
                                 DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String v = p.getValueAsString();
        return AxiomType.getAxiomType(v);
    }
}
