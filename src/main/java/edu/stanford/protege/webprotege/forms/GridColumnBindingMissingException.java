package edu.stanford.protege.webprotege.forms;



import edu.stanford.protege.webprotege.forms.field.GridColumnId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
public class GridColumnBindingMissingException extends RuntimeException {

    private GridColumnId columnId;

    public GridColumnBindingMissingException(GridColumnId columnId) {
        super("Grid column binding missing for " + columnId + ".  Form is not configured properly");
        this.columnId = checkNotNull(columnId);
    }


    private GridColumnBindingMissingException() {
    }

    @Nonnull
    public GridColumnId getColumnId() {
        return columnId;
    }
}
