package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.criteria.CompositeRootCriteria;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-08
 */

@AutoValue
public abstract class EntityFormSelector {

    public static EntityFormSelector get(@Nonnull @JsonProperty("projectId") ProjectId projectId,
                                         @Nonnull @JsonProperty("criteria") CompositeRootCriteria criteria,
                                         @Nonnull @JsonProperty("purpose") FormPurpose purpose,
                                         @Nonnull @JsonProperty("formId") FormId formId) {
        return new AutoValue_EntityFormSelector(projectId, purpose, criteria, formId);
    }

    @JsonCreator
    protected static EntityFormSelector create(@Nonnull @JsonProperty("projectId") ProjectId projectId,
                                               @Nonnull @JsonProperty("criteria") CompositeRootCriteria criteria,
                                               @Nullable @JsonProperty("purpose") FormPurpose purpose,
                                               @Nonnull @JsonProperty("formId") FormId formId) {
        FormPurpose normFormPurpose = purpose == null ? FormPurpose.ENTITY_EDITING : purpose;
        return get(projectId, criteria, normFormPurpose, formId);
    }

    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();

    @JsonProperty("purpose")
    public abstract FormPurpose getPurpose();

    @JsonProperty("criteria")
    public abstract CompositeRootCriteria getCriteria();

    @JsonProperty("formId")
    public abstract FormId getFormId();

}
