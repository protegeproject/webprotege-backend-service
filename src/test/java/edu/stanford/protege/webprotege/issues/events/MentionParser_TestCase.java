package edu.stanford.protege.webprotege.issues.events;

import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.issues.mention.*;
import org.hamcrest.Description;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.EntityType;

import java.util.List;

import static edu.stanford.protege.webprotege.issues.events.MentionParser_TestCase.ParsedEntityMentionMatcher.mentionedEntity;
import static edu.stanford.protege.webprotege.issues.events.MentionParser_TestCase.ParsedIssueMentionMatcher.mentionedIssue;
import static edu.stanford.protege.webprotege.issues.events.MentionParser_TestCase.ParsedRevisionMentionMatcher.mentionedRevision;
import static edu.stanford.protege.webprotege.issues.events.MentionParser_TestCase.ParsedUserIdMentionMatcher.mentionedUserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Sep 16
 */
public class MentionParser_TestCase {

    private MentionParser parser;

    @BeforeEach
    public void setUp() {
        parser = new MentionParser();
    }



    @Test
    public void shouldParseExactIssueMention() {
        List<ParsedMention> mentions = parser.parseMentions("#33");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedIssue(0, 3, 33)));
    }

    @Test
    public void shouldParseIssueMentionFollowedByFullStop() {
        List<ParsedMention> mentions = parser.parseMentions("#33.");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedIssue(0, 3, 33)));
    }

    @Test
    public void shouldParseIssueMentionFollowedByColon() {
        List<ParsedMention> mentions = parser.parseMentions("#33:");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedIssue(0, 3, 33)));
    }

    @Test
    public void shouldParseIssueMentionFollowedBySemiColon() {
        List<ParsedMention> mentions = parser.parseMentions("#33;");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedIssue(0, 3, 33)));
    }

    @Test
    public void shouldParseBracketedIssueMention() {
        List<ParsedMention> mentions = parser.parseMentions("(#33)");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedIssue(1, 4, 33)));
    }

    @Test
    public void shouldParseIssueMentionFollowedByBracket() {
        List<ParsedMention> mentions = parser.parseMentions("#33(");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedIssue(0, 3, 33)));
    }

    @Test
    public void shouldParseIssueMentionFollowedByComma() {
        List<ParsedMention> mentions = parser.parseMentions("#33,");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedIssue(0, 3, 33)));
    }

    @Test
    public void shouldParseIssueMentionFollowedBySquareBracket() {
        List<ParsedMention> mentions = parser.parseMentions("#33[");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedIssue(0, 3, 33)));
    }

    @Test
    public void shouldParseIssueMentionFollowedByBrace() {
        List<ParsedMention> mentions = parser.parseMentions("#33[");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedIssue(0, 3, 33)));
    }

    @Test
    public void shouldParseIssueMentionFollowedBySlash() {
        List<ParsedMention> mentions = parser.parseMentions("#33/");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedIssue(0, 3, 33)));
    }

    @Test
    public void shouldParseIssueInString() {
        List<ParsedMention> mentions = parser.parseMentions("See issue #33");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedIssue(10, 13, 33)));
    }

    @Test
    public void shouldNotParsePrefixedIssue() {
        List<ParsedMention> mentions = parser.parseMentions("prefix#33");
        MatcherAssert.assertThat(mentions.isEmpty(), Is.is(true));
    }



    @Test
    public void shouldParseExactRevisionMention() {
        List<ParsedMention> mentions = parser.parseMentions("R33");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedRevision(0, 3, 33L)));
    }

    @Test
    public void shouldParseRevisionMentionFollowedByFullStop() {
        List<ParsedMention> mentions = parser.parseMentions("R33.");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedRevision(0, 3, 33L)));
    }

    @Test
    public void shouldParseRevisionMentionFollowedByComma() {
        List<ParsedMention> mentions = parser.parseMentions("R33,");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedRevision(0, 3, 33L)));
    }

    @Test
    public void shouldParseRevisionMentionFollowedByColon() {
        List<ParsedMention> mentions = parser.parseMentions("R33:");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedRevision(0, 3, 33L)));
    }

    @Test
    public void shouldParseRevisionMentionFollowedBySemiColon() {
        List<ParsedMention> mentions = parser.parseMentions("R33;");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedRevision(0, 3, 33L)));
    }

    @Test
    public void shouldParseBracketedRevisionMention() {
        List<ParsedMention> mentions = parser.parseMentions("(R33)");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedRevision(1, 4, 33L)));
    }

    @Test
    public void shouldParseRevisionMentionFollowedByBracket() {
        List<ParsedMention> mentions = parser.parseMentions("R33(");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedRevision(0, 3, 33L)));
    }

    @Test
    public void shouldParseRevisionMentionFollowedBySquareBracket() {
        List<ParsedMention> mentions = parser.parseMentions("R33[");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedRevision(0, 3, 33L)));
    }

    @Test
    public void shouldParseRevisionMentionFollowedByBrace() {
        List<ParsedMention> mentions = parser.parseMentions("R33[");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedRevision(0, 3, 33L)));
    }

    @Test
    public void shouldParseRevisionMentionFollowedBySlash() {
        List<ParsedMention> mentions = parser.parseMentions("R33/");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedRevision(0, 3, 33L)));
    }

    @Test
    public void shouldParseRevisionInString() {
        List<ParsedMention> mentions = parser.parseMentions("See revision R33");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedRevision(13, 16, 33L)));
    }

    @Test
    public void shouldNotParsePrefixedRevision() {
        List<ParsedMention> mentions = parser.parseMentions("prefixR33");
        MatcherAssert.assertThat(mentions.isEmpty(), Is.is(true));
    }

    @Test
    public void shouldParseExactUserIdMention() {
        List<ParsedMention> mentions = parser.parseMentions("@Matthew");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedUserId(0, 8, "Matthew")));
    }

    @Test
    public void shouldParseExactUserIdMentionFollowedByComma() {
        List<ParsedMention> mentions = parser.parseMentions("@Matthew,");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedUserId(0, 8, "Matthew")));
    }

    @Test
    public void shouldParseUserIdThatLooksLikeEmailAddress() {
        List<ParsedMention> mentions = parser.parseMentions("@first.last@domain.com");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedUserId(0, 22, "first.last@domain.com")));
    }

    @Test
    public void shouldParseBracketedUserIdThatLooksLikeEmailAddress() {
        List<ParsedMention> mentions = parser.parseMentions("@{first.last@domain.com}");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedUserId(0, 24, "first.last@domain.com")));
    }

    @Test
    public void shouldParseExactBracketedUserIdMention() {
        List<ParsedMention> mentions = parser.parseMentions("@{Matthew H}");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedUserId(0, 12, "Matthew H")));
    }

    @Test
    public void shouldParseUserIdMentionInString() {
        List<ParsedMention> mentions = parser.parseMentions("See @Matthew for");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedUserId(4, 12, "Matthew")));
    }

    @Test
    public void shouldNotParsePrefixedUserIdMention() {
        List<ParsedMention> mentions = parser.parseMentions("Prefix@Matthew");
        MatcherAssert.assertThat(mentions.isEmpty(), Is.is(true));
    }

    @Test
    public void shouldParseMultipleMentions() {
        List<ParsedMention> mentions = parser.parseMentions("@Matthew please take a look at R33 and issue #44");
        MatcherAssert.assertThat(mentions, Matchers.hasItems(
                mentionedUserId(0, 8, "Matthew"),
                mentionedRevision(31, 34, 33),
                mentionedIssue(45, 48, 44)
        ));
    }

    @Test
    public void shouldParseExactClassMention() {
        String iri = "http://ontology/x";
        String mention = "Class(<" + iri + ">)";
        List<ParsedMention> mentions = parser.parseMentions(mention);
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedEntity(0, mention.length(), EntityType.CLASS, iri)));
    }

    @Test
    public void shouldParseClassMentionInString() {
        String iri = "http://ontology/x";
        String mention = "Class(<" + iri + ">)";
        List<ParsedMention> mentions = parser.parseMentions("See " + mention + " here");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedEntity(4, 4 + mention.length(), EntityType.CLASS, iri)));
    }

    @Test
    public void shouldParseClassMentionInBrackets() {
        String iri = "http://ontology/x";
        String mention = "Class(<" + iri + ">)";
        List<ParsedMention> mentions = parser.parseMentions("(" + mention + ")");
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedEntity(1, 1 + mention.length(), EntityType.CLASS, iri)));
    }

    @Test
    public void shouldParseExactObjectPropertyMention() {
        String iri = "http://ontology/x";
        String mention = "ObjectProperty(<" + iri + ">)";
        List<ParsedMention> mentions = parser.parseMentions(mention);
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedEntity(0, mention.length(), EntityType.OBJECT_PROPERTY, iri)));
    }

    @Test
    public void shouldParseExactDataPropertyMention() {
        String iri = "http://ontology/x";
        String mention = "DataProperty(<" + iri + ">)";
        List<ParsedMention> mentions = parser.parseMentions(mention);
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedEntity(0, mention.length(), EntityType.DATA_PROPERTY, iri)));
    }

    @Test
    public void shouldParseExactAnnotationPropertyMention() {
        String iri = "http://ontology/x";
        String mention = "AnnotationProperty(<" + iri + ">)";
        List<ParsedMention> mentions = parser.parseMentions(mention);
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedEntity(0, mention.length(), EntityType.ANNOTATION_PROPERTY, iri)));
    }

    @Test
    public void shouldParseExactNamedIndividualPropertyMention() {
        String iri = "http://ontology/x";
        String mention = "NamedIndividual(<" + iri + ">)";
        List<ParsedMention> mentions = parser.parseMentions(mention);
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedEntity(0, mention.length(), EntityType.NAMED_INDIVIDUAL, iri)));
    }

    @Test
    public void shouldParseExactDatatypePropertyMention() {
        String iri = "http://ontology/x";
        String mention = "Datatype(<" + iri + ">)";
        List<ParsedMention> mentions = parser.parseMentions(mention);
        MatcherAssert.assertThat(mentions, Matchers.hasItem(mentionedEntity(0, mention.length(), EntityType.DATATYPE, iri)));
    }






    public static class ParsedIssueMentionMatcher extends TypeSafeMatcher<ParsedMention> {

        private int expectedStartIndex;

        private int expectedEndIndex;

        private int expectedIssueNumber;

        public ParsedIssueMentionMatcher(int expectedStartIndex, int expectedEndIndex, int expectedIssueNumber) {
            this.expectedStartIndex = expectedStartIndex;
            this.expectedEndIndex = expectedEndIndex;
            this.expectedIssueNumber = expectedIssueNumber;
        }

        @Override
        protected boolean matchesSafely(ParsedMention item) {
            if(item.getStartIndex() != expectedStartIndex) {
                return false;
            }
            if(item.getEndIndex() != expectedEndIndex) {
                return false;
            }
            if(!(item.getParsedMention() instanceof IssueMention)) {
                return false;
            }
            IssueMention parsedMention = (IssueMention) item.getParsedMention();
            return parsedMention.getIssueNumber() == expectedIssueNumber;
        }

        @Override
        public void describeTo(Description description) {
            description.appendValue(
                    new ParsedMention(
                            new IssueMention(expectedIssueNumber),
                            expectedStartIndex,
                            expectedEndIndex)
            );
        }


        public static ParsedIssueMentionMatcher mentionedIssue(int startIndex, int endIndex, int issueNumber) {
            return new ParsedIssueMentionMatcher(startIndex, endIndex, issueNumber);
        }
    }
    

    public static class ParsedRevisionMentionMatcher extends TypeSafeMatcher<ParsedMention> {

        private int expectedStartIndex;

        private int expectedEndIndex;

        private long expectedRevision;

        public ParsedRevisionMentionMatcher(int expectedStartIndex, int expectedEndIndex, long expectedRevision) {
            this.expectedStartIndex = expectedStartIndex;
            this.expectedEndIndex = expectedEndIndex;
            this.expectedRevision = expectedRevision;
        }

        @Override
        protected boolean matchesSafely(ParsedMention item) {
            if(item.getStartIndex() != expectedStartIndex) {
                return false;
            }
            if(item.getEndIndex() != expectedEndIndex) {
                return false;
            }
            if(!(item.getParsedMention() instanceof RevisionMention)) {
                return false;
            }
            RevisionMention parsedMention = (RevisionMention) item.getParsedMention();
            return parsedMention.getRevisionNumber() == expectedRevision;
        }

        @Override
        public void describeTo(Description description) {
            description.appendValue(
                    new ParsedMention(
                            new RevisionMention(expectedRevision),
                            expectedStartIndex,
                            expectedEndIndex)
            );
        }


        public static ParsedRevisionMentionMatcher mentionedRevision(int startIndex, int endIndex, long revisionNumber) {
            return new ParsedRevisionMentionMatcher(startIndex, endIndex, revisionNumber);
        }
    }

    public static class ParsedUserIdMentionMatcher extends TypeSafeMatcher<ParsedMention> {

        private int expectedStartIndex;

        private int expectedEndIndex;

        private String expectedUserId;

        public ParsedUserIdMentionMatcher(int expectedStartIndex, int expectedEndIndex, String expectedUserId) {
            this.expectedStartIndex = expectedStartIndex;
            this.expectedEndIndex = expectedEndIndex;
            this.expectedUserId = expectedUserId;
        }

        @Override
        protected boolean matchesSafely(ParsedMention item) {
            if(item.getStartIndex() != expectedStartIndex) {
                return false;
            }
            if(item.getEndIndex() != expectedEndIndex) {
                return false;
            }
            if(!(item.getParsedMention() instanceof UserIdMention)) {
                return false;
            }
            UserIdMention parsedMention = (UserIdMention) item.getParsedMention();
            return parsedMention.getUserId().id().equals(expectedUserId);
        }

        @Override
        public void describeTo(Description description) {
            description.appendValue(new ParsedMention(new UserIdMention(UserId.valueOf(expectedUserId)), expectedStartIndex, expectedEndIndex));
        }

        public static ParsedUserIdMentionMatcher mentionedUserId(int startIndex, int endIndex, String userId) {
            return new ParsedUserIdMentionMatcher(startIndex, endIndex, userId);
        }
    }


    public static class ParsedEntityMentionMatcher extends TypeSafeMatcher<ParsedMention> {

        private int expectedStartIndex;

        private int expectedEndIndex;

        private EntityType<?> expectedEntityType;

        private String expectedIri;

        public ParsedEntityMentionMatcher(int expectedStartIndex,
                                          int expectedEndIndex,
                                          EntityType<?> expectedEntityType, String expectedIri) {
            this.expectedStartIndex = expectedStartIndex;
            this.expectedEndIndex = expectedEndIndex;
            this.expectedEntityType = expectedEntityType;
            this.expectedIri = expectedIri;
        }

        @Override
        protected boolean matchesSafely(ParsedMention item) {
            if(item.getStartIndex() != expectedStartIndex) {
                return false;
            }
            if(item.getEndIndex() != expectedEndIndex) {
                return false;
            }
            if(!(item.getParsedMention() instanceof EntityMention)) {
                return false;
            }
            EntityMention parsedMention = (EntityMention) item.getParsedMention();
            if(!parsedMention.getEntity().getEntityType().equals(expectedEntityType)) {
                return false;
            }
            return parsedMention.getEntity().getIRI().toString().equals(expectedIri);
        }

        @Override
        public void describeTo(Description description) {
            description.appendValue(
                    new ParsedMention(
                            new EntityMention(DataFactory.getOWLEntity(expectedEntityType, expectedIri)),
                            expectedStartIndex,
                            expectedEndIndex)
            );
        }


        public static ParsedEntityMentionMatcher mentionedEntity(int expectedStartIndex,
                                                               int expectedEndIndex,
                                                               EntityType<?> expectedEntityType, String expectedIri) {
            return new ParsedEntityMentionMatcher(expectedStartIndex, expectedEndIndex, expectedEntityType, expectedIri);
        }
    }



}
