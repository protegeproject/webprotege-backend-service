package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-11-09
 */
public class Proxy2PrimitiveFormControlDataConverter extends StdConverter<PrimitiveFormControlDataProxy, PrimitiveFormControlData> {

    @Override
    public PrimitiveFormControlData convert(PrimitiveFormControlDataProxy value) {
        return value.toPrimitiveFormControlData();
    }
}
