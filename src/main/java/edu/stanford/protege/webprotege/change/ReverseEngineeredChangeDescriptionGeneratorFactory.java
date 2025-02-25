package edu.stanford.protege.webprotege.change;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.change.matcher.ChangeMatcher;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
@ProjectSingleton
public class ReverseEngineeredChangeDescriptionGeneratorFactory {

    @Nonnull
    private final Set<ChangeMatcher> changeMatchers;

    @Nonnull
    private final OWLObjectStringFormatter formatter;

    @Inject
    public ReverseEngineeredChangeDescriptionGeneratorFactory(@Nonnull Set<ChangeMatcher> changeMatchers,
                                                              @Nonnull OWLObjectStringFormatter formatter) {
        this.changeMatchers = ImmutableSet.copyOf(changeMatchers);
        this.formatter = formatter;
    }

    public <S extends OWLEntity> ReverseEngineeredChangeDescriptionGenerator<S> get(String defaultDescription) {
        return new ReverseEngineeredChangeDescriptionGenerator<>(defaultDescription, changeMatchers, formatter);
    }
}
