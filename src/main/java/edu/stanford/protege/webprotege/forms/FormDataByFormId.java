package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.forms.data.FormData;

import javax.annotation.Nonnull;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-05-24
 */
public class FormDataByFormId {


    private Map<FormId, FormData> map;

    /**
     * Map FormIds to FormData.  This map should not contain null keys, but it
     * may contain null values
     */
    public FormDataByFormId(Map<FormId, FormData> map) {
        this.map = new LinkedHashMap<>(checkNotNull(map));
    }

    private FormDataByFormId() {
    }

    @Nonnull
    public Collection<FormId> getFormIds() {
        return ImmutableSet.copyOf(map.keySet());
    }

    @Nonnull
    public Optional<FormData> getFormData(@Nonnull FormId formId) {
        return Optional.ofNullable(map.get(formId));
    }

    public boolean contains(FormId formId) {
        return map.containsKey(formId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormDataByFormId)) {
            return false;
        }
        FormDataByFormId that = (FormDataByFormId) o;
        return map.equals(that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }

    @Override
    public String toString() {
        return "FormDataByFormId{" + map + '}';
    }
}
