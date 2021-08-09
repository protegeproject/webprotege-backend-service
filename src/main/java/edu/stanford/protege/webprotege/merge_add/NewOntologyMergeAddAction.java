package edu.stanford.protege.webprotege.merge_add;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.csv.DocumentId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.List;

@AutoValue

@JsonTypeName("NewOntologyMergeAdd")
public abstract class NewOntologyMergeAddAction implements ProjectAction<NewOntologyMergeAddResult> {

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
