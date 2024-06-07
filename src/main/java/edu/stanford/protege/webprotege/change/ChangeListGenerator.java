package edu.stanford.protege.webprotege.change;

import edu.stanford.protege.webprotege.app.TrackExecutionTime;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.owlapi.RenameMap;
import io.micrometer.core.annotation.Timed;
import org.checkerframework.checker.units.qual.Time;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 * <p>
 *     An interface to objects which can generate ontology changes.  This is used when applying changes to a project
 *     and allows the changes to be generated based on the state of the project.
 *
 * </p>
 */
public interface ChangeListGenerator<R> {

    /**
     * Generates ontology changes.
     * @param context The context for the change generation.  This contains information such as the id of the user
     * generating the changes.
     * @return The generated change list and main result bundled up in a {@link OntologyChangeList} object.
     */
    @TrackExecutionTime
    @Timed("changeListGenerator.generateChanges")
    OntologyChangeList<R> generateChanges(ChangeGenerationContext context);

    R getRenamedResult(R result, RenameMap renameMap);

    @Nonnull
    String getMessage(ChangeApplicationResult<R> result);

    ChangeRequestId getChangeRequestId();
}
