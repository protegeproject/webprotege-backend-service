package edu.stanford.protege.webprotege.mansyntax;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-20
 */
public class AutoCompletionResult {
    private static final AutoCompletionResult EMPTY_RESULT = new AutoCompletionResult();

    private final List<AutoCompletionChoice> choices;

    private final EditorPosition fromPosition;

    public AutoCompletionResult() {
        this(new ArrayList<AutoCompletionChoice>(), new EditorPosition(0, 0));
    }

    @JsonCreator
    public AutoCompletionResult(@JsonProperty("choices") List<AutoCompletionChoice> choices,
                                @JsonProperty("fromPosition") EditorPosition fromPosition) {
        this.choices = new ArrayList<AutoCompletionChoice>(checkNotNull(choices));
        this.fromPosition = checkNotNull(fromPosition);
    }

    public EditorPosition getFromPosition() {
        return fromPosition;
    }

    public List<AutoCompletionChoice> getChoices() {
        return new ArrayList<AutoCompletionChoice>(choices);
    }

    public static AutoCompletionResult emptyResult() {
        return EMPTY_RESULT;
    }

    @Override
    public int hashCode() {
        return "AutoCompletionResult".hashCode() + choices.hashCode() + fromPosition.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof AutoCompletionResult)) {
            return false;
        }
        AutoCompletionResult other = (AutoCompletionResult) o;
        return this.fromPosition.equals(other.fromPosition) && this.choices.equals(other.choices);
    }
}
