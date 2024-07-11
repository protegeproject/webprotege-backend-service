package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;
import java.util.Comparator;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-07-01
 */
@AutoValue
public abstract class FormDescriptorRecord implements Comparable<FormDescriptorRecord> {

    private static final Comparator<FormDescriptorRecord> comparingByOrdinal = Comparator.comparing(FormDescriptorRecord::getOrdinal);

    public static final String PROJECT_ID = "projectId";

    public static final String FORM = "form";

    public static final String ORDINAL = "ordinal";

    @JsonCreator
    public static FormDescriptorRecord get(@JsonProperty(PROJECT_ID) @Nonnull ProjectId projectId,
                                           @JsonProperty(FORM) FormDescriptor formDescriptor,
                                           @JsonProperty(ORDINAL) Integer ordinal) {
        return new AutoValue_FormDescriptorRecord(projectId,
                                                  formDescriptor == null ? FormDescriptor.empty(FormId.generate()) : formDescriptor,
                                                  ordinal == null ? 0 : ordinal);
    }

    @JsonProperty(PROJECT_ID)
    @Nonnull
    public abstract ProjectId getProjectId();

    @JsonProperty(FORM)
    @Nonnull
    public abstract FormDescriptor getFormDescriptor();

    @JsonProperty(ORDINAL)
    @Nonnull
    public abstract Integer getOrdinal();

    @Override
    public int compareTo(@Nonnull FormDescriptorRecord o) {
        return comparingByOrdinal.compare(this, o);
    }
}
