package edu.stanford.bmir.protege.web.server.csv;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2013
 */
public interface CSVReaderHandler {

    void handleRow(CSVRow csvRow);
}
