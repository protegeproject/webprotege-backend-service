package edu.stanford.protege.webprotege.merge;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.diff.DiffElement;
import edu.stanford.protege.webprotege.diff.DiffOperation;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.lang.LanguageManager;
import edu.stanford.protege.webprotege.project.Ontology;
import edu.stanford.protege.webprotege.project.UploadedOntologiesCache;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static edu.stanford.protege.webprotege.access.BuiltInAction.EDIT_ONTOLOGY;
import static edu.stanford.protege.webprotege.access.BuiltInAction.UPLOAD_AND_MERGE;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class ComputeProjectMergeActionHandler extends AbstractProjectActionHandler<ComputeProjectMergeAction, ComputeProjectMergeResult> {

    private static final Logger logger = LoggerFactory.getLogger(ComputeProjectMergeActionHandler.class);

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final Comparator<OWLAxiom> axiomComparator;

    @Nonnull
    private final LanguageManager languageManager;


    @Nonnull
    private final ProjectOntologiesBuilder projectOntologiesBuilder;

    private UploadedOntologiesCache uploadedOntologiesCache;

    @Inject
    public ComputeProjectMergeActionHandler(@Nonnull AccessManager accessManager,
                                            @Nonnull ProjectId projectId,
                                            @Nonnull Comparator<OWLAxiom> axiomComparator,
                                            @Nonnull LanguageManager languageManager,
                                            @Nonnull ProjectOntologiesBuilder projectOntologiesBuilder,
                                            UploadedOntologiesCache uploadedOntologiesCache) {
        super(accessManager);
        this.projectId = projectId;
        this.axiomComparator = axiomComparator;
        this.languageManager = languageManager;
        this.projectOntologiesBuilder = projectOntologiesBuilder;
        this.uploadedOntologiesCache = uploadedOntologiesCache;
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions(ComputeProjectMergeAction action) {
        return Arrays.asList(EDIT_ONTOLOGY, UPLOAD_AND_MERGE);
    }

    @Nonnull
    @Override
    public ComputeProjectMergeResult execute(@Nonnull ComputeProjectMergeAction action,
                                             @Nonnull ExecutionContext executionContext) {
        try {
            var documentId = action.getProjectDocumentId();

            var uploadedOntologies = uploadedOntologiesCache.getUploadedOntologies(documentId);
            var projectOntologies = projectOntologiesBuilder.buildProjectOntologies();

            var diffs = computeDiff(uploadedOntologies, projectOntologies);

            var transformedDiff = renderDiff(uploadedOntologies, diffs);

            return ComputeProjectMergeResult.create(transformedDiff);

        } catch(Exception e) {
            logger.info("An error occurred while merging ontologies", e);
            throw new RuntimeException(e);
        }
    }

    private Set<OntologyDiff> computeDiff(Collection<Ontology> uploadedRootOntology,
                                          Collection<Ontology> projectOntologies) {
        var diffCalculator = new OntologyDiffCalculator(
                new AnnotationDiffCalculator(),
                new AxiomDiffCalculator()
        );
        var modifiedOntologiesCalculator = new ModifiedProjectOntologiesCalculator(
                ImmutableSet.copyOf(projectOntologies),
                ImmutableSet.copyOf(uploadedRootOntology),
                diffCalculator
        );
        return modifiedOntologiesCalculator.getModifiedOntologyDiffs();
    }

    private List<DiffElement<String, String>> renderDiff(Collection<Ontology> uploadedOntologies,
                                                           Set<OntologyDiff> diffs) {

//        var uploadedProjectModule = new UploadedProjectModule(projectId,
//                                                              ImmutableSet.copyOf(uploadedOntologies),
//                                                              languageManager);
//        var uploadedOntologiesComponent = DaggerUploadedProjectComponent.builder()
//                                                                        .uploadedProjectModule(uploadedProjectModule)
//                                                                        .build();
//        var renderer = uploadedOntologiesComponent.getOwlObjectRenderer();
//
//        List<DiffElement<String, OWLAxiom>> diffElements = getDiffElements(diffs);
//        sortDiff(diffElements);
//
//        // Transform from OWLAxiom to String
//        List<DiffElement<String, String>> transformedDiff = new ArrayList<>();
//        for(DiffElement<String, OWLAxiom> element : diffElements) {
//            var html = renderer.render(element.getLineElement());
//            transformedDiff.add(new DiffElement<>(element.getDiffOperation(), element.getSourceDocument(), html));
//        }
//        uploadedOntologiesComponent.getProjectDisposablesManager().dispose();
//        return transformedDiff;
        throw new RuntimeException("Requires implementing");
    }

    private List<DiffElement<String, OWLAxiom>> getDiffElements(Set<OntologyDiff> diffs) {
        List<DiffElement<String, OWLAxiom>> diffElements = new ArrayList<>();
        for(OntologyDiff diff : diffs) {
            for(OWLAxiom ax : diff.getAxiomDiff()
                                  .getAdded()) {
                diffElements.add(new DiffElement<>(DiffOperation.ADD, "ontology", ax));
            }
            for(OWLAxiom ax : diff.getAxiomDiff()
                                  .getRemoved()) {
                diffElements.add(new DiffElement<>(DiffOperation.REMOVE, "ontology", ax));
            }
        }
        return diffElements;
    }

    private void sortDiff(List<DiffElement<String, OWLAxiom>> diffElements) {

        diffElements.sort((o1, o2) -> {
            int diff = axiomComparator.compare(o1.getLineElement(), o2.getLineElement());
            if(diff != 0) {
                return diff;
            }
            int opDiff = o1.getDiffOperation()
                           .compareTo(o2.getDiffOperation());
            if(opDiff != 0) {
                return opDiff;
            }
            return o1.getSourceDocument()
                     .compareTo(o2.getSourceDocument());
        });
    }


    @Nonnull
    @Override
    public Class<ComputeProjectMergeAction> getActionClass() {
        return ComputeProjectMergeAction.class;
    }
}
