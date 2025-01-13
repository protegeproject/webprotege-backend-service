package edu.stanford.protege.webprotege.forms.processor;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.forms.FormFrameBuilder;
import edu.stanford.protege.webprotege.forms.FormSubjectFactoryDescriptor;
import edu.stanford.protege.webprotege.forms.GridColumnBindingMissingException;
import edu.stanford.protege.webprotege.forms.data.GridRowData;
import edu.stanford.protege.webprotege.forms.field.GridColumnDescriptor;
import edu.stanford.protege.webprotege.forms.field.OwlBinding;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import jakarta.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
public class GridRowDataProcessor {

    @Nonnull
    private final Provider<FormFrameBuilder> formFrameBuilderProvider;

    @Nonnull
    private final GridCellDataProcessor gridCellDataProcessor;

    @Inject
    public GridRowDataProcessor(@Nonnull Provider<FormFrameBuilder> formFrameBuilderProvider,
                                @Nonnull GridCellDataProcessor gridCellDataProcessor) {
        this.formFrameBuilderProvider = checkNotNull(formFrameBuilderProvider);
        this.gridCellDataProcessor = checkNotNull(gridCellDataProcessor);
    }


    public void processGridRowData(@Nonnull OwlBinding binding,
                                   @Nonnull FormSubjectFactoryDescriptor rowSubjectFactoryDescriptor,
                                   @Nonnull ImmutableList<GridColumnDescriptor> columnDescriptors,
                                   @Nonnull FormFrameBuilder gridFrameBuilder,
                                   GridRowData gridRowData) {
        var rowFrameBuilder = formFrameBuilderProvider.get();

        var rowSubject = gridRowData.getSubject();
        // Set subject if it is present.  If it is not present
        // then a subject should be generated
        rowSubject.ifPresent(rowFrameBuilder::setSubject);
        rowFrameBuilder.setSubjectFactoryDescriptor(rowSubjectFactoryDescriptor);
        var rowCells = gridRowData.getCells();
        for(int i = 0; i < columnDescriptors.size(); i++) {
            var columnDescriptor = columnDescriptors.get(i);
            var gridCellData = rowCells.get(i);
            var columnId = columnDescriptor.getId();
            var cellBinding = columnDescriptor.getOwlBinding().orElseThrow(() -> new GridColumnBindingMissingException(columnId));
            gridCellDataProcessor.processGridCellData(rowFrameBuilder, cellBinding, gridCellData);
        }
        gridFrameBuilder.add(binding, rowFrameBuilder);
    }
}
