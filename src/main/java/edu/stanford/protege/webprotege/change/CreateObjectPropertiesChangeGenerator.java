package edu.stanford.protege.webprotege.change;



import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.msg.MessageFormatter;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;
import static org.semanticweb.owlapi.model.EntityType.OBJECT_PROPERTY;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */

public class CreateObjectPropertiesChangeGenerator extends AbstractCreateEntitiesChangeListGenerator<OWLObjectProperty, OWLObjectProperty> {

    @Nonnull
    private final OWLDataFactory dataFactory;

    public CreateObjectPropertiesChangeGenerator(@Nonnull OWLDataFactory dataFactory,
                                                 @Nonnull MessageFormatter msg,
                                                 @Nonnull DefaultOntologyIdManager defaultOntologyIdManager,
                                                 @Nonnull String sourceText,
                                                 @Nonnull String langTag,
                                                 @Nonnull ImmutableSet<OWLObjectProperty> parents) {
        super(OBJECT_PROPERTY, sourceText, langTag, parents, dataFactory, msg, defaultOntologyIdManager);
        this.dataFactory = checkNotNull(dataFactory);
    }

    @Override
    protected Set<? extends OWLAxiom> createParentPlacementAxioms(OWLObjectProperty freshEntity,
                                                                  ChangeGenerationContext context,
                                                                  ImmutableSet<OWLObjectProperty> parents) {
        return parents.stream()
                .map(parent -> dataFactory.getOWLSubObjectPropertyOfAxiom(freshEntity, parent))
                .collect(toSet());
    }
}
