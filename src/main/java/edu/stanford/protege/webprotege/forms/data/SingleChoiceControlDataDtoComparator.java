package edu.stanford.protege.webprotege.forms.data;

import com.google.common.collect.Comparators;

import jakarta.inject.Inject;
import java.util.Comparator;
import java.util.Optional;

public class SingleChoiceControlDataDtoComparator implements Comparator<SingleChoiceControlDataDto> {

    private static final Comparator<Optional<PrimitiveFormControlDataDto>> optionalComparator = Comparators.emptiesLast(
            PrimitiveFormControlDataDto::compareTo
    );

    @Inject
    public SingleChoiceControlDataDtoComparator() {
    }

    @Override
    public int compare(SingleChoiceControlDataDto o1, SingleChoiceControlDataDto o2) {
        return optionalComparator.compare(o1.getChoice(), o2.getChoice());
    }
}
