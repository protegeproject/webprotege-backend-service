package edu.stanford.protege.webprotege.issues.mention;

import edu.stanford.protege.webprotege.issues.Mention;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Sep 16
 */
public class ParsedMention {

    private final Mention parsedMention;

    private final int startIndex;

    private final int endIndex;

    public ParsedMention(Mention parsedMention, int startIndex, int endIndex) {
        this.parsedMention = parsedMention;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public Mention getParsedMention() {
        return parsedMention;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }


    @Override
    public String toString() {
        return toStringHelper("ParsedMention")
                .add("startIndex", startIndex)
                .add("endIndex", endIndex)
                .addValue(parsedMention)
                .toString();
    }
}
