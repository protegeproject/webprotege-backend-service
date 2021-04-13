package edu.stanford.bmir.protege.web.shared.merge_add;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.List;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("NewOntologyMergeAdd")
public abstract class NewOntologyMergeAddAction extends AbstractHasProjectAction<NewOntologyMergeAddResult> {

    @JsonCreator
    public static NewOntologyMergeAddAction create(@JsonProperty("projectId") ProjectId projectId,
                                                   @JsonProperty("documentId") DocumentId documentId,
                                                   @JsonProperty("iri") String iri,
                                                   @JsonProperty("ontologyList") List<OWLOntologyID> ontologyList) {
        return new AutoValue_NewOntologyMergeAddAction(projectId, documentId, iri, ontologyList);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    public abstract DocumentId getDocumentId();

    public abstract String getIri();

    public abstract List<OWLOntologyID> getOntologyList();
}
