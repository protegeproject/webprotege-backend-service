package edu.stanford.protege.webprotege.change;



import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.msg.MessageFormatter;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;
import static org.semanticweb.owlapi.model.EntityType.DATA_PROPERTY;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */

public class CreateDataPropertiesChangeGenerator extends AbstractCreateEntitiesChangeListGenerator<OWLDataProperty, OWLDataProperty> {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public CreateDataPropertiesChangeGenerator(@Nonnull OWLDataFactory dataFactory,
                                               @Nonnull MessageFormatter msg,
                                               @Nonnull DefaultOntologyIdManager defaultOntologyIdManager,
                                               @Nonnull String sourceText,
                                               @Nonnull String langTag,
                                               @Nonnull ImmutableSet<OWLDataProperty> parents) {
        super(DATA_PROPERTY, sourceText, langTag, parents, dataFactory, msg, defaultOntologyIdManager);
        this.dataFactory = checkNotNull(dataFactory);
    }

    @Override
    protected Set<? extends OWLAxiom> createParentPlacementAxioms(OWLDataProperty freshEntity,
                                                                  ChangeGenerationContext context,
                                                                  ImmutableSet<OWLDataProperty> parents) {
        return parents.stream()
                .map(parent -> dataFactory.getOWLSubDataPropertyOfAxiom(freshEntity, parent))
                .collect(toSet());
    }
}
