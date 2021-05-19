package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Optional;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @Type(OWLClassData.class),
        @Type(OWLObjectPropertyData.class),
        @Type(OWLDataPropertyData.class),
        @Type(OWLAnnotationPropertyData.class),
        @Type(OWLNamedIndividualData.class),
        @Type(OWLDatatypeData.class)
})
public abstract class OWLEntityData extends OWLPrimitiveData {

    public OWLEntity getEntity() {
        return (OWLEntity) getObject();
    }

    @JsonIgnore
    public boolean isIRIEmpty() {
        return getEntity().getIRI().length() == 0;
    }

    public int compareToIgnorePrefixNames(OWLEntityData other) {
        int prefixSepIndex = getPrefixSeparatorIndex();
        String comparisonString = getBrowserText().substring(prefixSepIndex != -1 ? prefixSepIndex : 0);
        int otherPrefixSepIndex = other.getPrefixSeparatorIndex();
        String otherComparisonString = other.getBrowserText().substring(otherPrefixSepIndex != -1 ? otherPrefixSepIndex : 0);
        return comparisonString.compareToIgnoreCase(otherComparisonString);
    }

    public abstract <R> R accept(OWLEntityDataVisitorEx<R> visitor);

    @Override
    public String getBrowserText() {
        IRI iri = getEntity().getIRI();
        if(FreshEntityIri.isFreshEntityIri(iri)) {
            return FreshEntityIri.parse(iri.toString()).getSuppliedName();
        }
        else {
            return getFirstShortForm(iri::toQuotedString);
        }
    }

    public int compareToIgnoreCase(OWLEntityData other) {
        return getBrowserText().compareToIgnoreCase(other.getBrowserText());
    }

    @JsonIgnore
    public int getPrefixSeparatorIndex() {
        return getBrowserText().indexOf(':');
    }

    @JsonIgnore
    @Override
    public String getUnquotedBrowserText() {
        String browserText = getBrowserText();
        if(browserText.startsWith("'") && browserText.endsWith("'")) {
            return browserText.substring(1, browserText.length() - 1);
        }
        else {
            return browserText;
        }
    }

    @Override
    public Optional<OWLAnnotationValue> asAnnotationValue() {
        return Optional.of(getEntity().getIRI());
    }

    @Override
    public Optional<OWLEntity> asEntity() {
        return Optional.of(getEntity());
    }

    public abstract boolean isDeprecated();
}
