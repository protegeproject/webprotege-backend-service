package edu.stanford.protege.webprotege.csv;

import edu.stanford.protege.webprotege.dispatch.Result;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2013
 */
public class GetCSVGridResult implements Result {

    private CSVGrid csvGrid;

    /**
     * For serialization purposes only
     */
    private GetCSVGridResult() {
    }

    public GetCSVGridResult(CSVGrid csvGrid) {
        this.csvGrid = csvGrid;
    }

    public CSVGrid getCSVGrid() {
        return csvGrid;
    }
}
