package edu.stanford.protege.webprotege.form;

import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.form.data.FormData;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class SetEntityFormsDataAction implements ProjectAction<SetEntityFormDataResult> {

    private ProjectId projectId;

    private OWLEntity entity;

    private ImmutableMap<FormId, FormData> pristineFormsData;

    private FormDataByFormId editedFormsData;

    public SetEntityFormsDataAction(@Nonnull ProjectId projectId,
                                    @Nonnull OWLEntity entity,
                                    @Nonnull ImmutableMap<FormId, FormData> pristineFormsData,
                                    @Nonnull FormDataByFormId editedFormsData) {
        this.projectId = checkNotNull(projectId);
        this.entity = checkNotNull(entity);
        this.pristineFormsData = checkNotNull(pristineFormsData);
        this.editedFormsData = checkNotNull(editedFormsData);
        checkArgument(editedFormsData.getFormIds().stream().allMatch(pristineFormsData::containsKey),
                      "Missing pristine forms data");
    }

    private SetEntityFormsDataAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public OWLEntity getEntity() {
        return entity;
    }

    @Nonnull
    public ImmutableMap<FormId, FormData> getPristineFormsData() {
        return pristineFormsData;
    }

    @Nonnull
    public FormDataByFormId getEditedFormsData() {
        return editedFormsData;
    }}
