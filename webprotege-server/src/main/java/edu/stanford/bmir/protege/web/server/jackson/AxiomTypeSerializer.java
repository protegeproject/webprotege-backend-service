package edu.stanford.bmir.protege.web.server.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.semanticweb.owlapi.model.AxiomType;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-08
 */
public class AxiomTypeSerializer extends StdSerializer<AxiomType> {

    public AxiomTypeSerializer() {
        super(AxiomType.class);
    }

    @Override
    public void serialize(AxiomType value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.getName());
    }
}
