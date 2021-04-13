package edu.stanford.bmir.protege.web.shared.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
@JsonTypeName("CheckManchesterSyntax")
@AutoValue
@GwtCompatible(serializable = true)
public abstract class CheckManchesterSyntaxFrameAction implements ProjectAction<CheckManchesterSyntaxFrameResult>, HasSubject<OWLEntity>, HasFreshEntities {


    @JsonCreator
    public static CheckManchesterSyntaxFrameAction create(@JsonProperty("projectId") ProjectId projectId,
                                                   @JsonProperty("subject") OWLEntity subject,
                                                   @JsonProperty("from") String from,
                                                   @JsonProperty("to") String to,
                                                   @JsonProperty("freshEntities") ImmutableSet<OWLEntityData> freshEntities) {
        return new AutoValue_CheckManchesterSyntaxFrameAction(projectId, subject, from, to, freshEntities);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Override
    public abstract OWLEntity getSubject();

    public abstract String getFrom();

    public abstract String getTo();

    public abstract ImmutableSet<OWLEntityData> getFreshEntities();
}
