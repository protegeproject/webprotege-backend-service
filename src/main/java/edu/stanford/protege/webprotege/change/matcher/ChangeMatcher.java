package edu.stanford.protege.webprotege.change.matcher;

import edu.stanford.protege.webprotege.change.OntologyChange;

import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public interface ChangeMatcher {

    Optional<ChangeSummary> getDescription(List<OntologyChange> changeData);
}
