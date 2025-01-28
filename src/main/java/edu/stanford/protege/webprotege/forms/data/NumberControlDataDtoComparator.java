package edu.stanford.protege.webprotege.forms.data;

import jakarta.inject.Inject;
import java.util.Comparator;

public class NumberControlDataDtoComparator implements Comparator<NumberControlDataDto> {

    @Inject
    public NumberControlDataDtoComparator() {
    }

    @Override
    public int compare(NumberControlDataDto o1, NumberControlDataDto o2) {
        return o1.compareTo(o2);
    }
}
