package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.common.LangTagFilter;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntityFrameFormDataModule {

    @Nonnull
    private final FormRegionOrderingIndex formRegionOrderingIndex;

    @Nonnull
    private final LangTagFilter langTagFilter;

    @Nonnull
    private final FormPageRequestIndex pageRequestIndex;

    @Nonnull
    private final FormRegionFilterIndex formRegionFilterIndex;


    public EntityFrameFormDataModule(@Nonnull FormRegionOrderingIndex formRegionOrderingIndex,
                                     @Nonnull LangTagFilter langTagFilter,
                                     @Nonnull FormPageRequestIndex pageRequestIndex, @Nonnull FormRegionFilterIndex formRegionFilterIndex) {
        this.formRegionOrderingIndex = checkNotNull(formRegionOrderingIndex);
        this.langTagFilter = checkNotNull(langTagFilter);
        this.pageRequestIndex = checkNotNull(pageRequestIndex);
        this.formRegionFilterIndex = checkNotNull(formRegionFilterIndex);
    }

    public EntityFrameFormDataModule() {
        this.formRegionOrderingIndex = FormRegionOrderingIndex.get(ImmutableSet.of());
        this.langTagFilter = LangTagFilter.get(ImmutableSet.of());
        this.pageRequestIndex = FormPageRequestIndex.create(ImmutableList.of());
        this.formRegionFilterIndex = FormRegionFilterIndex.get(ImmutableSet.of());
    }

    public FormRegionOrderingIndex provideFormRegionOrderingIndex() {
        return formRegionOrderingIndex;
    }

    public LangTagFilter provideLangTagFilter() {
        return langTagFilter;
    }

    public FormPageRequestIndex providePageRequestIndex() {
        return pageRequestIndex;
    }

    public FormRegionFilterIndex provideFormRegionFilterIndex() {
        return formRegionFilterIndex;
    }
}
