package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.authorization.ActionId;
import edu.stanford.protege.webprotege.forms.FormId;
import edu.stanford.protege.webprotege.perspective.PerspectiveId;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class HierarchyDescriptorRule {

    public Set<ActionId> getActions() {
        return Collections.emptySet();
    }

    public Optional<PerspectiveId> getPerspectiveId() {
        return Optional.empty();
    }

    public Optional<FormId> getFormId() {
        return Optional.empty();
    }

    public HierarchyDescriptor getDescriptor() {
        return null;
    }

}
