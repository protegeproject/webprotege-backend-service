package edu.stanford.protege.webprotege.ui;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.forms.FormId;
import edu.stanford.protege.webprotege.forms.field.FormRegionId;
import edu.stanford.protege.webprotege.perspective.NodeProperties;
import edu.stanford.protege.webprotege.perspective.PerspectiveId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record DisplayContext(
        @JsonProperty("projectId") @Nonnull ProjectId projectId,
        @JsonProperty("perspectiveId") @Nonnull PerspectiveId perspectiveId,
        @JsonProperty("viewId") @Nonnull ViewId viewId,
        @JsonProperty("viewNodeId") @Nonnull ViewNodeId viewNodeId,
        @JsonProperty("viewProperties") @Nonnull Map<String, String> viewProperties,
        @JsonProperty("formIds") List<FormId> formIds,
        @JsonProperty("formFieldId") @Nullable FormRegionId formFieldId,
        @JsonProperty("selectedPaths") @Nonnull List<List<OWLEntity>> selectedPaths) {

    @JsonCreator
    public DisplayContext(
            @JsonProperty("projectId") @Nonnull ProjectId projectId,
            @JsonProperty("perspectiveId") @Nonnull PerspectiveId perspectiveId,
            @JsonProperty("viewId") @Nonnull ViewId viewId,
            @JsonProperty("viewNodeId") @Nonnull ViewNodeId viewNodeId,
            @JsonProperty("viewProperties") @Nonnull Map<String, String> viewProperties,
            @JsonProperty("formIds") List<FormId> formIds,
            @JsonProperty("formFieldId") @Nullable FormRegionId formFieldId,
            @JsonProperty("selectedPaths") @Nonnull List<List<OWLEntity>> selectedPaths) {
        this.projectId = Objects.requireNonNull(projectId);
        this.perspectiveId = Objects.requireNonNull(perspectiveId);
        this.viewId = Objects.requireNonNull(viewId);
        this.viewNodeId = Objects.requireNonNull(viewNodeId);
        this.viewProperties = Objects.requireNonNull(viewProperties);
        this.formIds = List.copyOf(formIds);
        this.formFieldId = formFieldId;
        this.selectedPaths = selectedPaths.stream().map(List::copyOf).toList();
    }


}

