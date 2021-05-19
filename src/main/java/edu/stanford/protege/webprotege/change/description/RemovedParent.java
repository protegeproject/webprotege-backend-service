package edu.stanford.protege.webprotege.change.description;

import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-10
 */
@AutoValue
public abstract class RemovedParent implements StructuredChangeDescription {

    public static RemovedParent get(@Nonnull OWLEntity child,
                                    @Nonnull OWLEntity parent) {
        return new AutoValue_RemovedParent(child, parent);
    }

    @Nonnull
    @Override
    public String getTypeName() {
        return "RemovedParent";
    }

    @Nonnull
    @Override
    public String formatDescription(@Nonnull OWLObjectStringFormatter formatter) {
        return formatter.formatString("Removed parent %s from %s", getParent(), getChild());
    }

    @Nonnull
    public abstract OWLEntity getChild();

    @Nonnull
    public abstract OWLEntity getParent();
}
