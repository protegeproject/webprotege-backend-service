package edu.stanford.protege.webprotege.diff;

import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.mansyntax.render.HasGetRendering;
import org.semanticweb.owlapi.model.IRI;

import java.text.MessageFormat;

public class OrderingDiffElementRenderer {

    private final HasGetRendering rendering;

    public OrderingDiffElementRenderer(HasGetRendering rendering) {
        this.rendering = rendering;
    }

    public DiffElement<String, String> render(DiffElement<String, OrderChange> element, IRI entityParentIri) {
        OrderChange orderChange = element.getLineElement();
        String entityUri = element.getSourceDocument();
        String entityText = rendering.getRendering(
                DataFactory.getOWLClass(entityUri)
        ).getBrowserText();
        String parentText = rendering.getRendering(
                DataFactory.getOWLClass(entityParentIri)
        ).getBrowserText();

        String changeDescription;
        if (orderChange.newPreviousElement() != null) {
            String prevText = rendering.getRendering(
                    DataFactory.getOWLClass(
                            IRI.create(orderChange.newPreviousElement())
                    )
            ).getBrowserText();
            changeDescription = MessageFormat.format(
                    "Moved entity {0} after entity {1} under parent {2}",
                    entityText,
                    prevText,
                    parentText);
        } else if (orderChange.newNextElement() != null) {
            String nextText = rendering.getRendering(
                    DataFactory.getOWLClass(
                            IRI.create(orderChange.newNextElement())
                    )
            ).getBrowserText();
            changeDescription = MessageFormat.format(
                    "Moved entity {0} before entity {1} under parent {2}",
                    entityText,
                    nextText,
                    parentText
            );
        } else {
            changeDescription = MessageFormat.format(
                    "Reordered entity {0} under parent {1}",
                    entityText,
                    parentText
            );
        }

        return new DiffElement<>(element.getDiffOperation(), element.getSourceDocument(), changeDescription);
    }
}
