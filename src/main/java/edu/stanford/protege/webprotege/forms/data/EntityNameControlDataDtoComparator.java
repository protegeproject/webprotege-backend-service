package edu.stanford.protege.webprotege.forms.data;

import com.google.common.collect.Comparators;
import edu.stanford.protege.webprotege.entity.OWLEntityData;

import jakarta.inject.Inject;
import java.util.Comparator;
import java.util.Optional;

public class EntityNameControlDataDtoComparator implements Comparator<EntityNameControlDataDto> {

    private static final Comparator<Optional<OWLEntityData>> optionalEntityDataComparator = Comparators.emptiesLast(
            OWLEntityData::compareToIgnoreCase
    );

    @Inject
    public EntityNameControlDataDtoComparator() {
    }

    @Override
    public int compare(EntityNameControlDataDto o1, EntityNameControlDataDto o2) {
        return optionalEntityDataComparator.compare(o1.getEntity(), o2.getEntity());
    }
}
