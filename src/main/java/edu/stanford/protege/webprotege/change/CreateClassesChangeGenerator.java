package edu.stanford.protege.webprotege.change;



import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.msg.MessageFormatter;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import edu.stanford.protege.webprotege.util.ClassExpression;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;
import static org.semanticweb.owlapi.model.EntityType.CLASS;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */

public class CreateClassesChangeGenerator extends AbstractCreateEntitiesChangeListGenerator<OWLClass, OWLClass> {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public CreateClassesChangeGenerator(@Nonnull OWLDataFactory dataFactory,
                                        @Nonnull MessageFormatter msg,
                                        @Nonnull DefaultOntologyIdManager defaultOntologyIdManager,
                                        @Nonnull String sourceText,
                                        @Nonnull String langTag,
                                        @Nonnull ImmutableSet<OWLClass> parent, ChangeRequestId changeRequestId) {
        super(CLASS, sourceText, langTag, parent, dataFactory, msg, defaultOntologyIdManager, changeRequestId);
        this.dataFactory = checkNotNull(dataFactory);
    }

    @Override
    protected Set<? extends OWLAxiom> createParentPlacementAxioms(OWLClass freshEntity,
                                                                  ChangeGenerationContext context,
                                                                  ImmutableSet<OWLClass> parents) {
        return parents.stream()
                .filter(ClassExpression::isNotOwlThing)
                .map(parent -> dataFactory.getOWLSubClassOfAxiom(freshEntity, parent))
                .collect(toSet());
    }
}
