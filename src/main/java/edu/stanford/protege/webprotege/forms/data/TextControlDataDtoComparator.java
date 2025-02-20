package edu.stanford.protege.webprotege.forms.data;

import jakarta.inject.Inject;
import java.util.Comparator;

public class TextControlDataDtoComparator implements Comparator<TextControlDataDto> {

    @Inject
    public TextControlDataDtoComparator() {
    }

    @Override
    public int compare(TextControlDataDto o1, TextControlDataDto o2) {
        return o1.compareTo(o2);
    }
}
