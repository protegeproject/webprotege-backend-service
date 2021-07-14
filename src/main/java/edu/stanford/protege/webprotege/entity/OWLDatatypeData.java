package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.PrimitiveType;
import edu.stanford.protege.webprotege.shortform.DictionaryLanguage;
import edu.stanford.protege.webprotege.shortform.ShortForm;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntityVisitorEx;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
@AutoValue

@JsonTypeName("OWLDatatypeData")
public abstract class OWLDatatypeData extends OWLEntityData {

    public static OWLDatatypeData get(@Nonnull OWLDatatype datatype,
                                             @Nonnull ImmutableMap<DictionaryLanguage, String> shortForms) {
        return get(datatype, shortForms, false);
    }


    public static OWLDatatypeData get(@Nonnull OWLDatatype datatype,
                                             @Nonnull ImmutableMap<DictionaryLanguage, String> shortForms,
                                             boolean deprecated) {
        return get(datatype, toShortFormList(shortForms), deprecated);
    }

    @JsonCreator
    public static OWLDatatypeData get(@JsonProperty("entity") OWLDatatype datatype,
                                             @JsonProperty("shortForms") ImmutableList<ShortForm> shortForms,
                                             @JsonProperty("deprecated") boolean deprecated) {
        return new AutoValue_OWLDatatypeData(shortForms, deprecated, datatype);
    }

    @Nonnull
    @Override
    public abstract OWLDatatype getObject();

    @Override
    public PrimitiveType getType() {
        return PrimitiveType.DATA_TYPE;
    }

    @Override
    public OWLDatatype getEntity() {
        return getObject();
    }

    @Override
    public <R, E extends Throwable> R accept(OWLPrimitiveDataVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public <R> R accept(OWLEntityVisitorEx<R> visitor, R defaultValue) {
        return visitor.visit(getEntity());
    }

    @Override
    public <R> R accept(OWLEntityDataVisitorEx<R> visitor) {
        return  visitor.visit(this);
    }
}
