package edu.stanford.bmir.protege.web.server.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.HasSubject;
import edu.stanford.bmir.protege.web.server.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.server.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.server.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
@JsonTypeName("CheckManchesterSyntax")
@AutoValue

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
