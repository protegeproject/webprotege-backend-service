package edu.stanford.protege.webprotege.change;



import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.msg.MessageFormatter;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;
import static org.semanticweb.owlapi.model.EntityType.ANNOTATION_PROPERTY;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */

public class CreateAnnotationPropertiesChangeGenerator extends AbstractCreateEntitiesChangeListGenerator<OWLAnnotationProperty, OWLAnnotationProperty> {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public CreateAnnotationPropertiesChangeGenerator(@Nonnull OWLDataFactory dataFactory,
                                                     @Nonnull MessageFormatter msg,
                                                     @Nonnull DefaultOntologyIdManager defaultOntologyIdManager,
                                                     @Nonnull String sourceText,
                                                     @Nonnull String langTag,
                                                     @Nonnull ImmutableSet<OWLAnnotationProperty> parents,
                                                     ChangeRequestId changeRequestId) {
        super(ANNOTATION_PROPERTY, sourceText, langTag, parents, dataFactory, msg, defaultOntologyIdManager,
              changeRequestId);
        this.dataFactory = checkNotNull(dataFactory);
    }

    @Override
    protected Set<? extends OWLAxiom> createParentPlacementAxioms(OWLAnnotationProperty freshEntity,
                                                                  ChangeGenerationContext context,
                                                                  ImmutableSet<OWLAnnotationProperty> parents) {
        return parents.stream()
                .map(parent -> dataFactory.getOWLSubAnnotationPropertyOfAxiom(freshEntity, parent))
                .collect(toSet());
    }
}
