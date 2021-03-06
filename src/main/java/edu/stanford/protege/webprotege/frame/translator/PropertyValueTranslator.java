package edu.stanford.protege.webprotege.frame.translator;

import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.frame.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLEntityVisitorExAdapter;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-13
 */
class PropertyValueTranslator implements PlainPropertyValueVisitor<Set<OWLAxiom>> {

    private final OWLEntity subject;

    private final Mode mode;

    PropertyValueTranslator(OWLEntity subject,
                            Mode mode) {
        this.subject = subject;
        this.mode = mode;
    }

    @Override
    public Set<OWLAxiom> visit(final PlainPropertyClassValue propertyValue) {
        final OWLDataFactory df = DataFactory.get();
        final Set<OWLClassExpression> classExpressions = new HashSet<>();
        classExpressions.add(df.getOWLObjectSomeValuesFrom(propertyValue.getProperty(), propertyValue
                .getValue()
        ));
        if(mode == Mode.MAXIMAL) {
            classExpressions.add(df.getOWLObjectMinCardinality(1,
                                                               propertyValue.getProperty(),
                                                               propertyValue.getValue()));
        }
        return subject.accept(new OWLEntityVisitorExAdapter<>(null) {
            @Nonnull
            @Override
            public Set<OWLAxiom> visit(OWLClass subject) {
                Set<OWLAxiom> result = new HashSet<>();
                for(OWLClassExpression ce : classExpressions) {
                    result.add(df.getOWLSubClassOfAxiom(subject, ce));
                }
                return result;
            }

            @Nonnull
            @Override
            public Set<OWLAxiom> visit(OWLNamedIndividual subject) {
                Set<OWLAxiom> result = new HashSet<>();
                for(OWLClassExpression ce : classExpressions) {
                    result.add(df.getOWLClassAssertionAxiom(ce, subject));
                }
                return result;
            }
        });
    }

    @Override
    public Set<OWLAxiom> visit(final PlainPropertyIndividualValue propertyValue) {
        final OWLDataFactory df = DataFactory.get();
        final OWLClassExpression classExpression = df.getOWLObjectHasValue(propertyValue
                                                                                   .getProperty(),
                                                                           propertyValue
                                                                                   .getValue());
        return subject.accept(new OWLEntityVisitorExAdapter<>(null) {
            @Nonnull
            @Override
            public Set<OWLAxiom> visit(OWLClass subject) {
                return Collections.singleton(df.getOWLSubClassOfAxiom(subject, classExpression));
            }

            @Nonnull
            @Override
            public Set<OWLAxiom> visit(OWLNamedIndividual subject) {
                return Collections.singleton(df.getOWLObjectPropertyAssertionAxiom(propertyValue
                                                                                           .getProperty()
                        , subject, propertyValue
                                                                                           .getValue()
                ));
            }
        });
    }

    @Override
    public Set<OWLAxiom> visit(final PlainPropertyDatatypeValue propertyValue) {
        final OWLDataFactory df = DataFactory.get();
        final Set<OWLClassExpression> classExpressions = new HashSet<>();
        classExpressions.add(df.getOWLDataSomeValuesFrom(propertyValue.getProperty(), propertyValue
                .getValue()
        ));
        if(mode == Mode.MAXIMAL) {
            classExpressions.add(df.getOWLDataMinCardinality(1, propertyValue.getProperty(), propertyValue.getValue()));
        }
        return subject.accept(new OWLEntityVisitorExAdapter<>(null) {
            @Nonnull
            @Override
            public Set<OWLAxiom> visit(OWLClass subject) {
                Set<OWLAxiom> result = new HashSet<>();
                for(OWLClassExpression ce : classExpressions) {
                    result.add(df.getOWLSubClassOfAxiom(subject, ce));
                }
                return result;
            }

            @Nonnull
            @Override
            public Set<OWLAxiom> visit(OWLNamedIndividual subject) {
                Set<OWLAxiom> result = new HashSet<>();
                for(OWLClassExpression ce : classExpressions) {
                    result.add(df.getOWLClassAssertionAxiom(ce, subject));
                }
                return result;
            }
        });
    }

    @Override
    public Set<OWLAxiom> visit(final PlainPropertyLiteralValue propertyValue) {
        final OWLDataFactory df = DataFactory.get();
        final OWLClassExpression classExpression = df.getOWLDataHasValue(propertyValue
                                                                                 .getProperty()
                ,
                                                                         propertyValue.getValue());
        return subject.accept(new OWLEntityVisitorExAdapter<>(null) {
            @Nonnull
            @Override
            public Set<OWLAxiom> visit(OWLClass subject) {
                return Collections.singleton(df.getOWLSubClassOfAxiom(subject, classExpression));
            }

            @Nonnull
            @Override
            public Set<OWLAxiom> visit(OWLNamedIndividual subject) {
                return Collections.singleton(df.getOWLDataPropertyAssertionAxiom(propertyValue
                                                                                         .getProperty()
                        , subject, propertyValue
                                                                                         .getValue()));
            }
        });
    }

    @Override
    public Set<OWLAxiom> visit(PlainPropertyAnnotationValue propertyValue) {
        OWLDataFactory df = DataFactory.get();
        return Collections.singleton(df.getOWLAnnotationAssertionAxiom(propertyValue
                                                                               .getProperty()
                , subject.getIRI(),
                                                                       propertyValue.getValue()));
    }
}
