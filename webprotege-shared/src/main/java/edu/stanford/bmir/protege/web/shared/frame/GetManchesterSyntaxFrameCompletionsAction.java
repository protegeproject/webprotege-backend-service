package edu.stanford.bmir.protege.web.shared.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.Sets;
import edu.stanford.bmir.gwtcodemirror.client.EditorPosition;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/03/2014
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetManchesterSyntaxFrameCompletions")
public abstract class GetManchesterSyntaxFrameCompletionsAction implements ProjectAction<GetManchesterSyntaxFrameCompletionsResult>, HasSubject<OWLEntity>, HasFreshEntities {

    @JsonCreator
    public static GetManchesterSyntaxFrameCompletionsAction create(@JsonProperty("projectId") ProjectId projectId,
                                                                   @JsonProperty("subject") OWLEntity subject,
                                                                   @JsonProperty("syntax") String syntax,
                                                                   @JsonProperty("fromPos") EditorPosition fromPos,
                                                                   @JsonProperty("from") int from,
                                                                   @JsonProperty("freshEntities") Set<OWLEntityData> freshEntities,
                                                                   @JsonProperty("entityTypeSuggestionLimit") int entityTypeSuggestLimit) {
        return new AutoValue_GetManchesterSyntaxFrameCompletionsAction(projectId,
                                                             subject,
                                                             syntax,
                                                                       fromPos,
                                                             from,
                                                             freshEntities,
                                                             entityTypeSuggestLimit);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Override
    public abstract OWLEntity getSubject();

    public abstract String getSyntax();

    public abstract EditorPosition getFromPos();

    public abstract int getFrom();

    public abstract Set<OWLEntityData> getFreshEntities();

    public abstract int getEntityTypeSuggestLimit();
}
