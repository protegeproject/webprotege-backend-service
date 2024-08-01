package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.entity.OWLLiteralData;
import edu.stanford.protege.webprotege.entity.OWLPrimitiveData;
import edu.stanford.protege.webprotege.forms.PropertyNames;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import java.util.Optional;

@AutoValue
@JsonTypeName("LiteralFormControlDataDto")
public abstract class LiteralFormControlDataDto extends PrimitiveFormControlDataDto {

    @JsonCreator
    public static LiteralFormControlDataDto get(@JsonProperty(PropertyNames.LITERAL) @Nonnull OWLLiteralData literal) {
        return new AutoValue_LiteralFormControlDataDto(literal);
    }


    @JsonProperty(PropertyNames.LITERAL)
    @Nonnull
    public abstract OWLLiteralData getLiteral();

    @Nonnull
    @Override
    public PrimitiveFormControlData toPrimitiveFormControlData() {
        return LiteralFormControlData.get(getLiteral().getLiteral());
    }

    @Nonnull
    @Override
    public Optional<OWLLiteral> asLiteral() {
        return Optional.of(getLiteral().getLiteral());
    }

    @Nonnull
    @Override
    public OWLPrimitiveData getPrimitiveData() {
        return getLiteral();
    }
}
