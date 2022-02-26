package edu.stanford.protege.webprotege.index;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import java.util.Set;

public interface ClassFrameAxiomsIndex {

    /**
     * Gets the axiomsSource that constitute the class frame for the specified class.
     * @param subject The subject of the class frame
     * @param annotationsTreatment Specifies whether or not annotations should be included.
     * @return The axiomsSource that make up the class frame for the specified class
     */
    @Nonnull
    Set<OWLAxiom> getFrameAxioms(@Nonnull OWLClass subject,
                                 @Nonnull AnnotationsTreatment annotationsTreatment);

    enum AnnotationsTreatment {
        INCLUDE_ANNOTATIONS,
        EXCLUDE_ANNOTATIONS
    }
}
