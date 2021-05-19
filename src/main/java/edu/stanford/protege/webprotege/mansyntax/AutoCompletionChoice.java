package edu.stanford.protege.webprotege.mansyntax;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-20
 */
public class AutoCompletionChoice {

    private String text;

    private String displayText;

    private String cssClassName;

    private EditorPosition replaceTextFrom;

    private EditorPosition replaceTextTo;

    /**
     * For serialization purposes only
     */
    private AutoCompletionChoice() {
    }

    @JsonCreator
    public AutoCompletionChoice(@JsonProperty("text") String text,
                                @JsonProperty("displayText") String displayText,
                                @JsonProperty("cssClassName") String cssClassName,
                                @JsonProperty("replaceTextFrom") EditorPosition replaceTextFrom,
                                @JsonProperty("replaceTextTo") EditorPosition replaceTextTo) {
        this.text = checkNotNull(text);
        this.displayText = checkNotNull(displayText);
        this.cssClassName = checkNotNull(cssClassName);
        this.replaceTextFrom = checkNotNull(replaceTextFrom);
        this.replaceTextTo = checkNotNull(replaceTextTo);
    }

    public String getText() {
        return text;
    }

    public String getDisplayText() {
        return displayText;
    }

    public String getCssClassName() {
        return cssClassName;
    }

    public EditorPosition getReplaceTextFrom() {
        return replaceTextFrom;
    }

    public EditorPosition getReplaceTextTo() {
        return replaceTextTo;
    }

    @Override
    public int hashCode() {
        return "AutoCompletionChoice".hashCode() +
                text.hashCode() +
                displayText.hashCode() +
                cssClassName.hashCode() +
                replaceTextFrom.hashCode() +
                replaceTextTo.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof AutoCompletionChoice)) {
            return false;
        }
        AutoCompletionChoice other = (AutoCompletionChoice) o;
        return this.text.equals(other.text) &&
                this.displayText.equals(other.displayText) &&
                this.cssClassName.equals(other.cssClassName) &&
                this.replaceTextFrom.equals(other.replaceTextFrom) &&
                this.replaceTextTo.equals(other.replaceTextTo);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("AutoCompletionChoice")
                          .add("text", text)
                          .add("displayText", displayText)
                          .add("cssClassName", cssClassName)
                          .add("replaceFrom", replaceTextFrom)
                          .add("replaceTo", replaceTextTo)
                          .toString();
    }
}
