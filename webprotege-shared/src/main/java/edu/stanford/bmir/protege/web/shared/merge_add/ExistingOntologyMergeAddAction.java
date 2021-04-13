package edu.stanford.bmir.protege.web.shared.merge_add;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.List;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("ExistingOntologyMergeAdd")
public abstract class ExistingOntologyMergeAddAction implements ProjectAction<ExistingOntologyMergeAddResult> {

    @JsonCreator
    public static ExistingOntologyMergeAddAction create(@JsonProperty("projectId") ProjectId projectId,
                                                        @JsonProperty("documentId") DocumentId documentId,
                                                        @JsonProperty("selectedOntologies") List<OWLOntologyID> selectedOntologies,
                                                        @JsonProperty("targetOntology") OWLOntologyID targetOntology) {
        return new AutoValue_ExistingOntologyMergeAddAction(projectId, documentId, selectedOntologies, targetOntology);
    }

    public abstract DocumentId getDocumentId();

    public abstract List<OWLOntologyID> getSelectedOntologies();

    public abstract OWLOntologyID getTargetOntology();
}
