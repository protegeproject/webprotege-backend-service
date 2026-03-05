package edu.stanford.protege.webprotege.msg;

import javax.annotation.Nonnull;

/**
 * Utility for wrapping a message in HTML that triggers focusClickedEntity on click.
 */
public final class EntityFocusHtml {

    private EntityFocusHtml() {
    }

    /**
     * Wraps the description in a div that triggers window.focusClickedEntity on click.
     *
     * @param description the text content to display
     * @param entityIri   the IRI of the entity (used for onclick and title)
     * @return HTML string with clickable div
     */
    @Nonnull
    public static String wrap(@Nonnull String description, @Nonnull String entityIri) {
        return "<div style=\"cursor : pointer;\""
                + " onclick=\"window.focusClickedEntity && window.focusClickedEntity(event, '" + entityIri + "')\""
                + " title=\"Click to select entity " + entityIri + "\">"
                + description
                + "</div>";
    }
}
