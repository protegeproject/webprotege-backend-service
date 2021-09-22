package edu.stanford.protege.webprotege.forms.data;

import com.google.common.collect.Comparators;

import javax.inject.Inject;
import java.util.Comparator;

public class MultiChoiceControlDataDtoComparator implements Comparator<MultiChoiceControlDataDto> {

    private static final Comparator<Iterable<PrimitiveFormControlDataDto>> lexComparator = Comparators.lexicographical(
            PrimitiveFormControlDataDto::compareTo
    );

    @Inject
    public MultiChoiceControlDataDtoComparator() {
    }

    @Override
    public int compare(MultiChoiceControlDataDto o1, MultiChoiceControlDataDto o2) {
        return lexComparator.compare(o1.getValues(), o2.getValues());
    }
}
