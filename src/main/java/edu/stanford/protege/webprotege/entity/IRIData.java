package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.PrimitiveType;
import edu.stanford.protege.webprotege.shortform.DictionaryLanguage;
import edu.stanford.protege.webprotege.shortform.ShortForm;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEntityVisitorEx;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/12/2012
 */
@AutoValue

@JsonTypeName("IRIData")
public abstract class IRIData extends OWLPrimitiveData {

    public static IRIData get(@Nonnull IRI iri, @Nonnull ImmutableMap<DictionaryLanguage, String> shortForms) {
        return get(iri, toShortFormList(shortForms));
    }

    @JsonCreator
    public static IRIData get(@JsonProperty("iri") IRI iri,
                              @JsonProperty("shortForms") ImmutableList<ShortForm> shortForms) {
        return new AutoValue_IRIData(shortForms, iri);
    }

    @Nonnull
    @Override
    public abstract IRI getObject();

    @JsonProperty("iri")
    public IRI getIri() {
        return getObject();
    }

    @Override
    public PrimitiveType getType() {
        return PrimitiveType.IRI;
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
    public String getBrowserText() {
        return getObject().toString();
    }

    @JsonIgnore
    public boolean isHTTPLink() {
        return "http".equalsIgnoreCase(getObject().getScheme()) || "https".equalsIgnoreCase(getObject().getScheme());
    }

    @JsonIgnore
    public boolean isWikipediaLink() {
        return isHTTPLink() && getObject().toString().contains("wikipedia.org/wiki/");
    }

    @JsonIgnore
    @Override
    public String getUnquotedBrowserText() {
        return getObject().toString();
    }

    @Override
    public Optional<OWLAnnotationValue> asAnnotationValue() {
        return Optional.of(getObject());
    }

    @Override
    public Optional<OWLEntity> asEntity() {
        return Optional.empty();
    }

    @Override
    public Optional<IRI> asIRI() {
        return Optional.of(getObject());
    }

    @JsonIgnore
    @Override
    public boolean isDeprecated() {
        return super.isDeprecated();
    }
}
