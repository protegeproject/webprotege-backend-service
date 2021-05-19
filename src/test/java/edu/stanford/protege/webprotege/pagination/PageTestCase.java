package edu.stanford.protege.webprotege.pagination;

import org.junit.Test;

import java.util.Collections;

import static junit.framework.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/09/2013
 */
public class PageTestCase {

    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsIllegalArgumentExceptionIfPageNumberIsGreaterThanPageCount() {
        Page.create(2, 1, Collections.emptyList(), 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsIndexOfOutBoundsExceptionForPageNumberOfZero() {
        Page.create(0, 1, Collections.emptyList(), 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsIllegalArgumentExceptionForPageCountOfZero() {
        Page.create(1, 0, Collections.emptyList(), 100);
    }

    @Test(expected = NullPointerException.class)
    public void constructorThrowsNullPointerExceptionForNullElements() {
        Page.create(1, 1, null, 100);
    }


    @Test
    public void getPageNumberReturnsSuppliedPageNumber() {
        Page<String> p = Page.create(2, 2, Collections.emptyList(), 100);
        assertEquals(2, p.getPageNumber());
    }
}
