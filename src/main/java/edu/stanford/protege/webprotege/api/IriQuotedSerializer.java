package edu.stanford.protege.webprotege.api;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.semanticweb.owlapi.model.IRI;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Apr 2018
 */
@Deprecated
public class IriQuotedSerializer extends StdSerializer<IRI> {

    public IriQuotedSerializer() {
        super(IRI.class);
    }

    @Override
    public void serialize(IRI iri, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(iri.toQuotedString());
    }
}
