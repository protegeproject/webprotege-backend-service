package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.ProjectRequest;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.OWLEntity;


@JsonTypeName("CheckManchesterSyntax")
public record LocalCheckManchesterSyntaxFrameAction(ProjectId projectId, OWLEntity subject, String from, String to, ImmutableSet<OWLEntityData> freshEntities)  implements ProjectRequest<LocalCheckManchesterSyntaxFrameResult>, HasFreshEntities {

        public static final String CHANNEL = "webprotege.frames.CheckManchesterSyntax";

        @JsonCreator
        public static LocalCheckManchesterSyntaxFrameAction create(@JsonProperty("projectId") ProjectId projectId,
                                                                                                    @JsonProperty("subject") OWLEntity subject,
                                                                                                    @JsonProperty("from") String from, @JsonProperty("to") String to,
                                                                                                    @JsonProperty("freshEntities") ImmutableSet<OWLEntityData> freshEntities) {
            return new LocalCheckManchesterSyntaxFrameAction(projectId, subject, from, to, freshEntities);

        }

        public String getChannel() {
            return "webprotege.frames.CheckManchesterSyntax";
        }

    @NotNull
    @Override
    public ProjectId projectId() {
        return projectId;
    }

}
