package edu.stanford.protege.webprotege.csv;

import edu.stanford.protege.webprotege.dispatch.Result;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/05/2013
 */
public class ImportCSVFileResult implements Result {

    private final int rowCount;

    public ImportCSVFileResult(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getRowCount() {
        return rowCount;
    }
}
