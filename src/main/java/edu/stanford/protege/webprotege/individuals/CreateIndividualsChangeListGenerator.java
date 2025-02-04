package edu.stanford.protege.webprotege.individuals;



import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.change.AbstractCreateEntitiesChangeListGenerator;
import edu.stanford.protege.webprotege.change.ChangeGenerationContext;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.msg.MessageFormatter;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import edu.stanford.protege.webprotege.util.ClassExpression;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;
import static org.semanticweb.owlapi.model.EntityType.NAMED_INDIVIDUAL;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */

public class CreateIndividualsChangeListGenerator extends AbstractCreateEntitiesChangeListGenerator<OWLNamedIndividual, OWLClass> {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public CreateIndividualsChangeListGenerator(@Nonnull OWLDataFactory dataFactory,
                                                @Nonnull MessageFormatter msg,
                                                @Nonnull DefaultOntologyIdManager defaultOntologyIdManager,
                                                @Nonnull ImmutableSet<OWLClass> parents,
                                                @Nonnull String sourceText,
                                                @Nonnull String langTag,
                                                @Nonnull ChangeRequestId changeRequestId) {
        super(NAMED_INDIVIDUAL, sourceText, langTag, parents, dataFactory, msg, defaultOntologyIdManager,
              changeRequestId);
        this.dataFactory = checkNotNull(dataFactory);
    }

    @Override
    protected Set<? extends OWLAxiom> createParentPlacementAxioms(OWLNamedIndividual freshEntity,
                                                                  ChangeGenerationContext context,
                                                                  ImmutableSet<OWLClass> parents) {
        return parents.stream()
                .filter(ClassExpression::isNotOwlThing)
                .map(parent -> dataFactory.getOWLClassAssertionAxiom(parent, freshEntity))
                .collect(toSet());
    }
}
