package edu.stanford.protege.webprotege.change.description;

import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-10
 */
@AutoValue
public abstract class SwitchedRelationshipProperty implements StructuredChangeDescription {

    public static SwitchedRelationshipProperty get(@Nonnull OWLObject subject,
                                                   @Nonnull OWLProperty fromProperty,
                                                   @Nonnull OWLProperty toProperty,
                                                   @Nonnull OWLObject value) {
        return new AutoValue_SwitchedRelationshipProperty(subject,
                                                          fromProperty,
                                                          toProperty,
                                                          value);
    }

    @Nonnull
    public abstract OWLObject getSubject();

    @Nonnull
    public abstract OWLProperty getFromProperty();

    @Nonnull
    public abstract OWLProperty getToProperty();

    @Nonnull
    public abstract OWLObject getValue();

    @Nonnull
    @Override
    public String getTypeName() {
        return "SwitchedRelationshipProperty";
    }

    @Nonnull
    @Override
    public String formatDescription(@Nonnull OWLObjectStringFormatter formatter) {
        return formatter.formatString("Switch relationship property from %s to %s on %s",
                                      getFromProperty(),
                                      getToProperty(),
                                      getSubject());
    }
}
