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
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLEntityVisitorEx;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
@AutoValue

@JsonTypeName("OWLAnnotationPropertyData")
public abstract class OWLAnnotationPropertyData extends OWLPropertyData {

    public static final int BEFORE = -1;

    public static OWLAnnotationPropertyData get(@Nonnull OWLAnnotationProperty property,
                                            @Nonnull ImmutableMap<DictionaryLanguage, String> shortForms) {
        return get(property, shortForms, false);
    }


    public static OWLAnnotationPropertyData get(@Nonnull OWLAnnotationProperty property,
                                            @Nonnull ImmutableMap<DictionaryLanguage, String> shortForms,
                                            boolean deprecated) {
        return get(property, toShortFormList(shortForms), deprecated);
    }

    @JsonCreator
    public static OWLAnnotationPropertyData get(@JsonProperty("entity") OWLAnnotationProperty property,
                                            @JsonProperty("shortForms") ImmutableList<ShortForm> shortForms,
                                            @JsonProperty("deprecated") boolean deprecated) {
        return new AutoValue_OWLAnnotationPropertyData(shortForms, deprecated, property);
    }

    @Nonnull
    @Override
    public abstract OWLAnnotationProperty getObject();

    @Override
    public OWLAnnotationProperty getEntity() {
        return getObject();
    }

    @Override
    public <R, E extends Throwable> R accept(OWLPrimitiveDataVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public <R> R accept(OWLEntityVisitorEx<R> visitor, R defaultValue) {
        return visitor.visit(getObject());
    }

    @Override
    public boolean isOWLAnnotationProperty() {
        return true;
    }

    @Override
    public PrimitiveType getType() {
        return PrimitiveType.ANNOTATION_PROPERTY;
    }

    @Override
    public <R> R accept(OWLEntityDataVisitorEx<R> visitor) {
        return visitor.visit(this);
    }
}
