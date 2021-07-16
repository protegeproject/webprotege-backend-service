package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.Action;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
@JsonTypeName("CreateNewProject")
public class CreateNewProjectAction implements Action<CreateNewProjectResult> {

    private NewProjectSettings newProjectSettings;

    /**
     * For serialization purposes only
     */
    private CreateNewProjectAction() {
    }

    @JsonCreator
    public CreateNewProjectAction(@JsonProperty("newProjectSettings") NewProjectSettings newProjectSettings) {
        this.newProjectSettings = checkNotNull(newProjectSettings);
    }

    public static CreateNewProjectAction create(NewProjectSettings newProjectSettings) {
        return new CreateNewProjectAction(newProjectSettings);
    }

    public NewProjectSettings getNewProjectSettings() {
        return newProjectSettings;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(newProjectSettings);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CreateNewProjectAction)) {
            return false;
        }
        CreateNewProjectAction other = (CreateNewProjectAction) obj;
        return this.newProjectSettings.equals(other.newProjectSettings);
    }

    @Override
    public String toString() {
        return toStringHelper("CreateNewProjectAction")
                .addValue(newProjectSettings)
                .toString();
    }
}
