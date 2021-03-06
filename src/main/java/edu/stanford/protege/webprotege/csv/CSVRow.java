package edu.stanford.protege.webprotege.csv;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2013
 */
public class CSVRow implements Iterable<String> {

    private List<String> columns;

    private CSVRow() {
    }

    /**
     * Constructs a CSVRow.
     * @param columns The columns in the row.  Not {@code null}.
     * @throws NullPointerException if {@code columns} is {@code null} or any element of {@code columns} is {@code null}.
     */
    public CSVRow(List<String> columns) {
        for(String item : checkNotNull(columns)) {
            checkNotNull(item, "Item in specified rowItems is null");
        }
        this.columns = new ArrayList<String>(columns);
    }

    /**
     * Gets the column count in this row.
     * @return The column count.
     */
    public int size() {
        return columns.size();
    }

    /**
     * Gets the value of the specified column.
     * @param columnIndex The index of the column.  Must be greater or equal to zero and less than the column count.
     * @return The value of the column.  Not {@code null}.  May be the empty string.
     */
    public String getColumnValue(int columnIndex) {
        checkElementIndex(columnIndex, columns.size(), "columnIndex out of bounds");
        return columns.get(columnIndex);
    }

    @Override
    public Iterator<String> iterator() {
        return Collections.unmodifiableList(columns).iterator();
    }
}
