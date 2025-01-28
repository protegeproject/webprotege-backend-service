package edu.stanford.protege.webprotege.forms.processor;

import edu.stanford.protege.webprotege.forms.FormFrameBuilder;
import edu.stanford.protege.webprotege.forms.FormSubjectFactoryDescriptorMissingException;
import edu.stanford.protege.webprotege.forms.data.GridControlData;
import edu.stanford.protege.webprotege.forms.data.GridRowData;
import edu.stanford.protege.webprotege.forms.field.OwlBinding;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
public class GridControlDataProcessor {

    @Nonnull
    private final GridRowDataProcessor gridRowDataProcessor;

    @Inject
    public GridControlDataProcessor(@Nonnull GridRowDataProcessor gridRowDataProcessor) {
        this.gridRowDataProcessor = gridRowDataProcessor;
    }

    public void processGridControlData(@Nonnull OwlBinding binding,
                                       @Nonnull GridControlData gridControlData,
                                       @Nonnull FormFrameBuilder formFrameBuilder) {
        var gridControlDescriptor = gridControlData.getDescriptor();
        // The subject factory descriptor MUST exists otherwise the form descriptor is not properly configured
        var rowSubjectFactoryDescriptor = gridControlDescriptor.getSubjectFactoryDescriptor()
                                                               .orElseThrow(FormSubjectFactoryDescriptorMissingException::new);
        for(GridRowData gridRowData : gridControlData.getRows()) {
            gridRowDataProcessor.processGridRowData(binding,
                                                    rowSubjectFactoryDescriptor,
                                                    gridControlDescriptor.getColumns(),
                                                    formFrameBuilder, gridRowData);
        }
    }
}
