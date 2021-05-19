package edu.stanford.protege.webprotege.perspective;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-20
 */
public enum Direction {

    @JsonProperty("row")
    ROW,

    @JsonProperty("column")
    COLUMN;

    public static Direction getDefaultDirection() {
        return ROW;
    }

    /**
     * Gets the direction that is perpendicular to this direction.
     * @return The perpendicular direction.  If this direction is {@link #ROW} then the return value will be
     * {@link #COLUMN}.  If this direction is {@link #COLUMN} then the return value will be {@link #ROW}.
     */
    public Direction getPerpendicularDirection() {
        if(this == ROW) {
            return COLUMN;
        }
        else {
            return ROW;
        }
    }
}
