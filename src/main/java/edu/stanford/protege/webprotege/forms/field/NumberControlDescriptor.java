package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.forms.PropertyNames;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Jun 2017
 */
@JsonTypeName(NumberControlDescriptor.TYPE)
public class NumberControlDescriptor implements FormControlDescriptor {

    protected static final String TYPE = "NUMBER";

    @Nonnull
    private String format = "#.#";

    private int length = 6;

    private LanguageMap placeholder = LanguageMap.empty();

    @Nonnull
    private NumberControlRange range = NumberControlRange.range(Double.MIN_VALUE,
                                                                NumberControlRange.BoundType.INCLUSIVE,
                                                                Double.MAX_VALUE,
                                                                NumberControlRange.BoundType.INCLUSIVE);

    @Nonnull
    private NumberControlType widgetType = NumberControlType.PLAIN;


    private NumberControlDescriptor() {
    }

    @JsonCreator
    public NumberControlDescriptor(@JsonProperty(PropertyNames.FORMAT) @Nonnull String format,
                                   @JsonProperty(PropertyNames.RANGE) @Nonnull NumberControlRange range,
                                   @JsonProperty(PropertyNames.WIDGET_TYPE) @Nonnull NumberControlType widgetType,
                                   @JsonProperty(PropertyNames.LENGTH) int length,
                                   @JsonProperty(PropertyNames.PLACEHOLDER) @Nonnull LanguageMap placeholder) {
        this.format = checkNotNull(format);
        this.range = checkNotNull(range);
        this.widgetType = checkNotNull(widgetType);
        this.length = length;
        this.placeholder = checkNotNull(placeholder);
    }

    public static String getTypeId() {
        return TYPE;
    }

    @Override
    public <R> R accept(@Nonnull FormControlDescriptorVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof NumberControlDescriptor)) {
            return false;
        }
        NumberControlDescriptor other = (NumberControlDescriptor) obj;
        return this.format.equals(other.format) && this.range.equals(other.range) && this.widgetType.equals(other.widgetType) && this.length == other.length && this.placeholder.equals(
                other.placeholder);
    }

    @JsonIgnore
    @Nonnull
    @Override
    public String getAssociatedType() {
        return TYPE;
    }

    @Nonnull
    @JsonProperty(PropertyNames.FORMAT)
    public String getFormat() {
        return format;
    }

    @JsonProperty(PropertyNames.LENGTH)
    public int getLength() {
        return length;
    }

    @JsonProperty(PropertyNames.PLACEHOLDER)
    public LanguageMap getPlaceholder() {
        return placeholder;
    }

    @Nonnull
    @JsonProperty(PropertyNames.RANGE)
    public NumberControlRange getRange() {
        return range;
    }

    @Nonnull
    @JsonProperty(PropertyNames.WIDGET_TYPE)
    public NumberControlType getWidgetType() {
        return widgetType;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(format, range, widgetType, length, placeholder);
    }
}
