package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.HasLexicalForm;
import edu.stanford.protege.webprotege.PrimitiveType;
import edu.stanford.protege.webprotege.shortform.ShortForm;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEntityVisitorEx;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
@AutoValue

@JsonTypeName("OWLLiteralData")
public abstract class OWLLiteralData extends OWLPrimitiveData implements HasLexicalForm {

    @JsonCreator
    public static OWLLiteralData get(@JsonProperty("literal") @Nonnull OWLLiteral literal) {
        return new AutoValue_OWLLiteralData(literal);
    }

    @JsonIgnore
    @Nonnull
    @Override
    public abstract OWLLiteral getObject();

    @JsonIgnore
    @Override
    public PrimitiveType getType() {
        return PrimitiveType.LITERAL;
    }


    public OWLLiteral getLiteral() {
        return getObject();
    }

    @JsonIgnore
    @Override
    public String getBrowserText() {
        OWLLiteral literal = getLiteral();
        return literal.getLiteral();
    }

    @JsonIgnore
    @Override
    public String getUnquotedBrowserText() {
        return getBrowserText();
    }

    @JsonIgnore
    @Override
    public String getLexicalForm() {
        return getLiteral().getLiteral();
    }

    @JsonIgnore
    public boolean hasLang() {
        return getLiteral().hasLang();
    }

    @JsonIgnore
    @Nonnull
    public String getLang() {
        return getLiteral().getLang();
    }

    @Override
    public <R, E extends Throwable> R accept(OWLPrimitiveDataVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public <R> R accept(OWLEntityVisitorEx<R> visitor, R defaultValue) {
        return defaultValue;
    }

    @Override
    public Optional<OWLAnnotationValue> asAnnotationValue() {
        return Optional.of(getLiteral());
    }

    @Override
    public Optional<OWLEntity> asEntity() {
        return Optional.empty();
    }

    @Override
    public Optional<OWLLiteral> asLiteral() {
        return Optional.of(getLiteral());
    }

    @JsonIgnore
    @Override
    public boolean isDeprecated() {
        return super.isDeprecated();
    }

    @JsonIgnore
    @Override
    public ImmutableList<ShortForm> getShortForms() {
        return ImmutableList.of();
    }
}
