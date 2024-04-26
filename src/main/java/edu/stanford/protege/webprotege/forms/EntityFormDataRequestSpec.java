package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.common.LangTagFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntityFormDataRequestSpec {

    @Nonnull
    private final FormRegionOrderingIndex formRegionOrderingIndex;

    @Nonnull
    private final LangTagFilter langTagFilter;

    @Nonnull
    private final FormPageRequestIndex pageRequestIndex;

    @Nonnull
    private final FormRegionFilterIndex formRegionFilterIndex;


    public EntityFormDataRequestSpec(@Nonnull FormRegionOrderingIndex formRegionOrderingIndex,
                                     @Nonnull LangTagFilter langTagFilter,
                                     @Nonnull FormPageRequestIndex pageRequestIndex, @Nonnull FormRegionFilterIndex formRegionFilterIndex) {
        this.formRegionOrderingIndex = checkNotNull(formRegionOrderingIndex);
        this.langTagFilter = checkNotNull(langTagFilter);
        this.pageRequestIndex = checkNotNull(pageRequestIndex);
        this.formRegionFilterIndex = checkNotNull(formRegionFilterIndex);
    }

    public EntityFormDataRequestSpec() {
        this.formRegionOrderingIndex = FormRegionOrderingIndex.get(ImmutableSet.of());
        this.langTagFilter = LangTagFilter.get(ImmutableSet.of());
        this.pageRequestIndex = FormPageRequestIndex.create(ImmutableList.of());
        this.formRegionFilterIndex = FormRegionFilterIndex.get(ImmutableSet.of());
    }

    @Bean
    public FormRegionOrderingIndex formRegionOrderingIndex() {
        return formRegionOrderingIndex;
    }

    @Bean
    public LangTagFilter langTagFilter() {
        return langTagFilter;
    }

    @Bean
    public FormPageRequestIndex pageRequestIndex() {
        return pageRequestIndex;
    }

    @Bean
    public FormRegionFilterIndex formRegionFilterIndex() {
        return formRegionFilterIndex;
    }
}
