package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import edu.stanford.protege.webprotege.forms.data.PrimitiveFormControlData;

import javax.annotation.Nonnull;

public class PrimitiveFormControlDataConverter {

    private final LiteralConverter literalConverter;

    private final IriConverter iriConverter;

    private final EntityConverter entityConverter;

    private final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    public PrimitiveFormControlDataConverter(LiteralConverter literalConverter, IriConverter iriConverter, EntityConverter entityConverter) {
        this.literalConverter = literalConverter;
        this.iriConverter = iriConverter;
        this.entityConverter = entityConverter;
    }

    @Nonnull
    public JsonNode convert(PrimitiveFormControlData data) {
        var lit = data.asLiteral();
        if (lit.isPresent()) {
            var literal = lit.get();
            return literalConverter.convert(literal);
        }
        var iri = data.asIri();
        if (iri.isPresent()) {
            var i = iri.get();
            return iriConverter.convert(i);
        }
        var ent = data.asEntity();
        if (ent.isPresent()) {
            var entity = ent.get();
            return entityConverter.convert(entity);
        }
        return nodeFactory.nullNode();
    }

}
