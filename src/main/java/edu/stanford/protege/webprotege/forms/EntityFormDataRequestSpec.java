package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.authorization.Capability;
import edu.stanford.protege.webprotege.common.LangTagFilter;
import edu.stanford.protege.webprotege.forms.field.FormRegionId;
import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.context.annotation.Bean;

import javax.annotation.Nonnull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntityFormDataRequestSpec {

    @Nonnull
    private final FormRootSubject subject;

    @Nonnull
    private final FormRegionOrderingIndex formRegionOrderingIndex;

    @Nonnull
    private final LangTagFilter langTagFilter;

    @Nonnull
    private final FormPageRequestIndex pageRequestIndex;

    @Nonnull
    private final FormRegionFilterIndex formRegionFilterIndex;

    private final UserCapabilities userCapabilities;

    private final FormRegionAccessRestrictionsList formRegionAccessRestrictions;


    public EntityFormDataRequestSpec(@Nonnull FormRootSubject subject,
                                     @Nonnull FormRegionOrderingIndex formRegionOrderingIndex,
                                     @Nonnull LangTagFilter langTagFilter,
                                     @Nonnull FormPageRequestIndex pageRequestIndex,
                                     @Nonnull FormRegionFilterIndex formRegionFilterIndex,
                                     @Nonnull UserCapabilities userCapabilities,
                                     @Nonnull FormRegionAccessRestrictionsList formRegionAccessRestrictions) {
        this.subject = subject;
        this.formRegionOrderingIndex = checkNotNull(formRegionOrderingIndex);
        this.langTagFilter = checkNotNull(langTagFilter);
        this.pageRequestIndex = checkNotNull(pageRequestIndex);
        this.formRegionFilterIndex = checkNotNull(formRegionFilterIndex);
        this.userCapabilities = userCapabilities;
        this.formRegionAccessRestrictions = formRegionAccessRestrictions;
    }

    public EntityFormDataRequestSpec(FormRootSubject subject) {
        this.subject = subject;
        this.formRegionAccessRestrictions = new FormRegionAccessRestrictionsList(List.of());
        this.formRegionOrderingIndex = FormRegionOrderingIndex.get(ImmutableSet.of());
        this.langTagFilter = LangTagFilter.get(ImmutableSet.of());
        this.pageRequestIndex = FormPageRequestIndex.create(ImmutableList.of());
        this.formRegionFilterIndex = FormRegionFilterIndex.get(ImmutableSet.of());
        this.userCapabilities = new UserCapabilities(Set.of());
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

    @Bean
    public UserCapabilities userCapabilities() {
        return userCapabilities;
    }

    @Bean
    public FormRegionAccessRestrictionsList formRegionAccessRestrictions() {
        return formRegionAccessRestrictions;
    }

    @Bean
    public FormRootSubject subject() {
        return subject;
    }

    /**
     * A wrapper for a generic set of capabilities.
     * @param capabilities The capabilities
     */
    public record UserCapabilities(Set<Capability> capabilities) {

        public UserCapabilities(Set<Capability> capabilities) {
            this.capabilities = Objects.requireNonNull(capabilities);
        }

        public Set<FormRegionCapability> getFormRegionCapabilities(FormRegionId formRegionId) {
            return capabilities.stream()
                    .filter(c -> c instanceof FormRegionCapability)
                    .map(c -> (FormRegionCapability) c)
                    .filter(c -> c.formRegionId().equals(formRegionId))
                    .collect(Collectors.toSet());
        }
    }

    /**
     * A wrapper for a generic list of {@link FormRegionAccessRestriction}
     * @param formRegionAccessRestrictions
     */
    public record FormRegionAccessRestrictionsList(List<FormRegionAccessRestriction> formRegionAccessRestrictions) {

        public FormRegionAccessRestrictionsList(List<FormRegionAccessRestriction> formRegionAccessRestrictions) {
            this.formRegionAccessRestrictions = Objects.requireNonNull(formRegionAccessRestrictions);
        }

        public boolean hasAccessRestrictions(FormRegionId formRegionId, String capabilityId) {
            return formRegionAccessRestrictions.stream()
                    .filter(a -> a.formRegionId().equals(formRegionId))
                    .anyMatch(r -> r.capabilityId().equals(capabilityId));
        }
    }

    public record FormRootSubject(OWLEntity subject) {

    }
}
