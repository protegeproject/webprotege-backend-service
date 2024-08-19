package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum FormRegionOrderingDirection {

    @JsonProperty("asc") ASC(1),

    @JsonProperty("desc") DESC(-1);

    int dir;

    FormRegionOrderingDirection(int dir) {
        this.dir = dir;
    }

    public int getDir() {
        return dir;
    }

    public boolean isAscending() {
        return this == ASC;
    }
}
