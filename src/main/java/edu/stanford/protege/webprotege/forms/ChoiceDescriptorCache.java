package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.forms.field.ChoiceDescriptorDto;
import edu.stanford.protege.webprotege.forms.field.ChoiceListSourceDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

@FormDataBuilderSession
public class ChoiceDescriptorCache {


    @Nonnull
    private final Map<ChoiceListSourceDescriptor, ImmutableList<ChoiceDescriptorDto>> descriptorCache = new HashMap<>();

    @Nonnull
    private final ChoiceDescriptorDtoSupplier choiceDescriptorDtoSupplier;

    @Inject
    public ChoiceDescriptorCache(@Nonnull ChoiceDescriptorDtoSupplier choiceDescriptorDtoSupplier) {
        this.choiceDescriptorDtoSupplier = checkNotNull(choiceDescriptorDtoSupplier);
    }

    @Nonnull
    public ImmutableList<ChoiceDescriptorDto> getChoices(@Nonnull ChoiceListSourceDescriptor sourceDescriptor) {
        var cachedChoices = descriptorCache.get(sourceDescriptor);
        if (cachedChoices == null) {
            cachedChoices = choiceDescriptorDtoSupplier.getChoices(sourceDescriptor);
            descriptorCache.put(sourceDescriptor, cachedChoices);
        }
        return cachedChoices;
    }
}