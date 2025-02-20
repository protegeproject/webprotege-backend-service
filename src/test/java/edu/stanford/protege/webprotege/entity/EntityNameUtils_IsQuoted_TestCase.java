package edu.stanford.protege.webprotege.entity;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/11/2013
 */
public class EntityNameUtils_IsQuoted_TestCase {

    @Test
public void shouldThrowNullPointerExceptionIfStringIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        EntityNameUtils.isQuoted(null);
     });
}

    @Test
    public void shouldReturnFalseForEmptyString() {
        boolean quoted = EntityNameUtils.isQuoted("");
        assertEquals(false, quoted);
    }

    @Test
    public void shouldReturnFalseForSingleSingleQuote() {
        boolean quoted = EntityNameUtils.isQuoted("'");
        assertEquals(false, quoted);
    }


    @Test
    public void shouldReturnFalseForSingleQuoteOnlyAtStart() {
        boolean quoted = EntityNameUtils.isQuoted("'a");
        assertEquals(false, quoted);
    }


    @Test
    public void shouldReturnFalseForSingleQuoteOnlyAtEnd() {
        boolean quoted = EntityNameUtils.isQuoted("a'");
        assertEquals(false, quoted);
    }

    @Test
    public void shouldReturnTrueForEmptyStringSurroundedBySingleQuotes() {
        boolean quoted = EntityNameUtils.isQuoted("''");
        assertEquals(true, quoted);
    }

    @Test
    public void shouldReturnTrueForSingleQuoteAtStartAndAtEnd() {
        boolean quoted = EntityNameUtils.isQuoted("'a'");
        assertEquals(true, quoted);
    }
}
