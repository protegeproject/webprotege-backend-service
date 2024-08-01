package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-11-09
 */
public class PrimitiveFormControlData2ProxyConverter extends StdConverter<PrimitiveFormControlData, PrimitiveFormControlDataProxy> {

    @Override
    public PrimitiveFormControlDataProxy convert(PrimitiveFormControlData value) {
        return value.toPrimitiveFormControlDataProxy();
    }
}
