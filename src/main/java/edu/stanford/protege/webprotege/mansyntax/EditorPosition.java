package edu.stanford.protege.webprotege.mansyntax;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-20
 */
public class EditorPosition {

    private final int lineNumber;

    private final int columnNumber;

    @JsonCreator
    public EditorPosition(@JsonProperty("lineNumber") int lineNumber,
                          @JsonProperty("columnNumber") int columnNumber) {
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    @Override
    public int hashCode() {
        return "EditorPosition".hashCode() + lineNumber + columnNumber * 13;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EditorPosition)) {
            return false;
        }
        EditorPosition other = (EditorPosition) o;
        return this.lineNumber == other.lineNumber && this.columnNumber == other.columnNumber;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("EditorPosition")
                          .add("lineNumber", lineNumber)
                          .add("columnNumber", columnNumber)
                          .toString();
    }
}
