package edu.stanford.protege.webprotege.obo;

import edu.stanford.protege.webprotege.change.AddAxiomChange;
import edu.stanford.protege.webprotege.change.FixedChangeListGenerator;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.change.RemoveAxiomChange;
import edu.stanford.protege.webprotege.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import edu.stanford.protege.webprotege.common.UserId;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static edu.stanford.protege.webprotege.obo.OboUtil.isXRefProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class TermXRefsManager {

    @Nonnull
    private final ChangeManager changeManager;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final XRefExtractor xrefExtractor;

    @Nonnull
    private final AnnotationToXRefConverter xrefConverter;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologies;

    @Nonnull
    private final AnnotationAssertionAxiomsBySubjectIndex annotationAssertions;

    @Inject
    public TermXRefsManager(@Nonnull ChangeManager changeManager,
                            @Nonnull OWLDataFactory dataFactory,
                            @Nonnull XRefExtractor xrefExtractor,
                            @Nonnull AnnotationToXRefConverter xrefConverter,
                            @Nonnull ProjectOntologiesIndex projectOntologies,
                            @Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertions) {
        this.changeManager = changeManager;
        this.dataFactory = dataFactory;
        this.xrefExtractor = xrefExtractor;
        this.xrefConverter = xrefConverter;
        this.projectOntologies = projectOntologies;
        this.annotationAssertions = annotationAssertions;
    }

    @Nonnull
    public List<OBOXRef> getXRefs(@Nonnull OWLEntity term) {
        return xrefExtractor.getXRefs(term);
    }

    public void setXRefs(UserId userId, OWLEntity term, List<OBOXRef> xrefs) {
        var subject = term.getIRI();
        var changes = new ArrayList<OntologyChange>();
        projectOntologies.getOntologyIds()
                         .forEach(ontId -> {
                             annotationAssertions.getAxiomsForSubject(subject, ontId)
                                                 .forEach(existingAx -> {
                                                     if(isXRefProperty(existingAx.getProperty())) {
                                                         xrefs.stream()
                                                              .map(xrefConverter::toAnnotation)
                                                              .map(annotation -> toAnnotationAssertion(subject,
                                                                                                       annotation))
                                                              .map(ax -> AddAxiomChange.of(ontId, ax))
                                                              .forEach(changes::add);
                                                         var removeAx = RemoveAxiomChange.of(ontId,
                                                                                             existingAx);
                                                         changes.add(removeAx);
                                                     }
                                                 });
                         });
        changeManager.applyChanges(userId,
                                   new FixedChangeListGenerator<>(changes, term, "Edited term XRefs"));
    }

    private OWLAnnotationAssertionAxiom toAnnotationAssertion(IRI subject, OWLAnnotation annotation) {
        return dataFactory.getOWLAnnotationAssertionAxiom(
                subject,
                annotation);
    }

}
