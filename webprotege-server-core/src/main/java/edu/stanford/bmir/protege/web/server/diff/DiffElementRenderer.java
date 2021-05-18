package edu.stanford.bmir.protege.web.server.diff;

import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.renderer.HasHtmlBrowserText;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/15
 */
public class DiffElementRenderer<S extends Serializable> {

    private HasHtmlBrowserText renderer;

    private OntologyChangeVisitorEx<String> visitor;

    public DiffElementRenderer(HasHtmlBrowserText ren) {
        this.renderer = ren;
        visitor = new OntologyChangeVisitorEx<>() {

            @Override
            public String visit(@Nonnull AddAxiomChange change)  {
                return renderer.getHtmlBrowserText(change.getAxiom());
            }

            @Override
            public String visit(@Nonnull RemoveAxiomChange change)  {
                return renderer.getHtmlBrowserText(change.getAxiom());
            }

            @Override
            public String visit(@Nonnull AddOntologyAnnotationChange change)  {
                return renderer.getHtmlBrowserText(change.getAnnotation());
            }

            @Override
            public String visit(@Nonnull RemoveOntologyAnnotationChange change)  {
                return renderer.getHtmlBrowserText(change.getAnnotation());
            }

            @Override
            public String visit(@Nonnull AddImportChange change)  {
                return renderer.getHtmlBrowserText(change.getImportsDeclaration().getIRI());
            }

            @Override
            public String visit(@Nonnull RemoveImportChange change)  {
                return renderer.getHtmlBrowserText(change.getImportsDeclaration().getIRI());
            }

            @Override
            public String getDefaultReturnValue() {
                throw new RuntimeException();
            }
        };
    }

    public DiffElement<S, String> render(DiffElement<S, OntologyChange> element) {
        OntologyChange lineElement = element.getLineElement();
        return new DiffElement<>(
                element.getDiffOperation(),
                element.getSourceDocument(),
                renderData(lineElement)
        );
    }

    public String renderData(OntologyChange change) {
        return change.accept(visitor);
    }
}
