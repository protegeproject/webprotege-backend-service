package edu.stanford.protege.webprotege.forms;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.common.PageRequest;
import edu.stanford.protege.webprotege.forms.data.FormSubject;
import edu.stanford.protege.webprotege.forms.field.FormRegionId;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-22
 */
public class FormPageRequestIndex {

    @Nonnull
    private final ImmutableMap<Key, FormPageRequest> indexMap;

    public FormPageRequestIndex(@Nonnull ImmutableMap<Key, FormPageRequest> indexMap) {
        this.indexMap = checkNotNull(indexMap);
    }

    @Nonnull
    public static FormPageRequestIndex create(@Nonnull ImmutableList<FormPageRequest> pageRequests) {
        checkNotNull(pageRequests);
        Map<Key, FormPageRequest> map = new HashMap<>();
        for (FormPageRequest pageRequest : pageRequests) {
            map.put(Key.get(pageRequest.subject(), pageRequest.regionId(), pageRequest.sourceType()),
                    pageRequest);
        }
        return new FormPageRequestIndex(ImmutableMap.copyOf(map));
    }

    @Nonnull
    public PageRequest getPageRequest(FormSubject formSubject, FormRegionId id, FormPageRequest.SourceType sourceType) {
        var formPageRequest = indexMap.get(Key.get(formSubject, id, sourceType));
        if (formPageRequest != null) {
            return formPageRequest.pageRequest();
        }
        else {
            return PageRequest.requestPageWithSize(1, FormPageRequest.DEFAULT_PAGE_SIZE);
        }
    }

    @AutoValue
    public static abstract class Key {

        public static Key get(@Nonnull FormSubject subject,
                              @Nonnull FormRegionId formRegionId,
                              @Nonnull FormPageRequest.SourceType sourceType) {
            return new AutoValue_FormPageRequestIndex_Key(subject, formRegionId, sourceType);
        }

        @Nonnull
        public abstract FormSubject getFormSubject();

        @Nonnull
        public abstract FormRegionId getFormRegionId();

        @Nonnull
        public abstract FormPageRequest.SourceType getSourceType();
    }


}
