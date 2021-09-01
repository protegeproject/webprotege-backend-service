package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.entity.IRIData;
import edu.stanford.protege.webprotege.entity.OWLPrimitiveData;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import java.util.Optional;

@AutoValue

@JsonTypeName("IriFormControlDataDto")
public abstract class IriFormControlDataDto extends PrimitiveFormControlDataDto {

    @JsonCreator
    public static IriFormControlDataDto get(@JsonProperty("iri") @Nonnull IRIData iriData) {
        return new AutoValue_IriFormControlDataDto(iriData);
    }

    @Nonnull
    public abstract IRIData getIri();

    @Nonnull
    @Override
    public PrimitiveFormControlData toPrimitiveFormControlData() {
        return IriFormControlData.get(getIri().getObject());
    }

    @Nonnull
    @Override
    public Optional<OWLLiteral> asLiteral() {
        return Optional.empty();
    }

    @Nonnull
    @Override
    public OWLPrimitiveData getPrimitiveData() {
        return getIri();
    }
}
