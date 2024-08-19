package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.Objects;
import com.google.common.collect.Range;
import edu.stanford.protege.webprotege.forms.PropertyNames;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Jun 2017
 */
public class NumberControlRange {

    private static final NumberControlRange ANY_NUMBER = new NumberControlRange(Double.MIN_VALUE,
                                                                                BoundType.INCLUSIVE,
                                                                                Double.MAX_VALUE,
                                                                                BoundType.INCLUSIVE);

    private Double lowerBound = Double.MIN_VALUE;

    private BoundType lowerBoundType = BoundType.INCLUSIVE;

    private Double upperBound = Double.MAX_VALUE;

    private BoundType upperBoundType = BoundType.INCLUSIVE;

    @JsonCreator
    private NumberControlRange(@JsonProperty(PropertyNames.LOWER_BOUND) double lowerBound,
                               @JsonProperty(PropertyNames.LOWER_BOUND_TYPE) BoundType lowerBoundType,
                               @JsonProperty(PropertyNames.UPPER_BOUND) double upperBound,
                               @JsonProperty(PropertyNames.UPPER_BOUND_TYPE) BoundType upperBoundType) {
        this.lowerBound = lowerBound;
        this.lowerBoundType = lowerBoundType;
        this.upperBound = upperBound;
        this.upperBoundType = upperBoundType;
    }


    private NumberControlRange() {
    }

    public static NumberControlRange range(double lowerBound,
                                           BoundType lowerBoundType,
                                           double upperBound,
                                           BoundType upperBoundType) {
        return new NumberControlRange(lowerBound, lowerBoundType, upperBound, upperBoundType);
    }

    public static NumberControlRange all() {
        return ANY_NUMBER;
    }

    @JsonIgnore
    public boolean isAnyNumber() {
        return this.equals(ANY_NUMBER);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof NumberControlRange)) {
            return false;
        }
        NumberControlRange other = (NumberControlRange) obj;
        return this.lowerBound.equals(other.lowerBound) && this.lowerBoundType.equals(other.lowerBoundType) && this.upperBound.equals(
                other.upperBound) && this.upperBoundType.equals(other.upperBoundType);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(lowerBound, lowerBoundType, upperBound, upperBoundType);
    }

    public Range<Double> toRange() {
        return Range.range(this.getLowerBound(),
                           this.getLowerBoundType() == NumberControlRange.BoundType.INCLUSIVE ? com.google.common.collect.BoundType.CLOSED : com.google.common.collect.BoundType.OPEN,
                           this.getUpperBound(),
                           this.getUpperBoundType() == NumberControlRange.BoundType.INCLUSIVE ? com.google.common.collect.BoundType.CLOSED : com.google.common.collect.BoundType.OPEN);
    }

    @JsonProperty(PropertyNames.LOWER_BOUND)
    public double getLowerBound() {
        return lowerBound;
    }

    @JsonProperty(PropertyNames.LOWER_BOUND_TYPE)
    public BoundType getLowerBoundType() {
        return lowerBoundType;
    }

    @JsonProperty(PropertyNames.UPPER_BOUND)
    public double getUpperBound() {
        return upperBound;
    }

    @JsonProperty(PropertyNames.UPPER_BOUND_TYPE)
    public BoundType getUpperBoundType() {
        return upperBoundType;
    }

    @Override
    public String toString() {
        return toStringHelper("NumberControlRange").add("lowerBound", lowerBound)
                                                   .add("lowerBoundType", lowerBoundType)
                                                   .add("upperBound", upperBound)
                                                   .add("upperBoundType", upperBoundType)
                                                   .toString();
    }

    public enum BoundType {
        INCLUSIVE(">=", "<="), EXCLUSIVE(">", "<");

        private final String lowerSymbol;

        private final String upperSymbol;

        BoundType(String lowerSymbol, String upperSymbol) {
            this.lowerSymbol = lowerSymbol;
            this.upperSymbol = upperSymbol;
        }

        public String getLowerSymbol() {
            return lowerSymbol;
        }

        public String getUpperSymbol() {
            return upperSymbol;
        }
    }
}
