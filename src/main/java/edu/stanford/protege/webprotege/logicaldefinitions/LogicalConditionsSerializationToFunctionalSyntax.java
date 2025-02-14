package edu.stanford.protege.webprotege.logicaldefinitions;

import edu.stanford.protege.webprotege.entity.OWLClassData;
import edu.stanford.protege.webprotege.frame.PropertyClassValue;
import org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.functional.renderer.FunctionalSyntaxObjectRenderer;
import org.semanticweb.owlapi.model.*;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.*;

public class LogicalConditionsSerializationToFunctionalSyntax {

    private static  final OWLOntologyManager man = OWLManager.createOWLOntologyManager();

    public static List<String> serializeNecessaryConditions(List<PropertyClassValue> necessaryConditions, OWLClassData clsData) throws OWLOntologyCreationException {
        var ont = man.createOntology();
        List<String> serializedAxes = new ArrayList<String>();

        for (PropertyClassValue necessaryCond : necessaryConditions) {
            //not sure, if we can reuse the same writer for different axioms. If we can purge it at the end, yes. If yes, you can put this outside of the for loop
            StringWriter writer = new StringWriter();
            FunctionalSyntaxObjectRenderer r = new FunctionalSyntaxObjectRenderer(ont, writer);

            var objectSomeValuesFrom = ObjectSomeValuesFrom(
                    OWLFunctionalSyntaxFactory.ObjectProperty(necessaryCond.getProperty().getEntity().getIRI()),
                    OWLFunctionalSyntaxFactory.Class(necessaryCond.getValue().getEntity().getIRI())
            );
            var subClassOfAxiom = SubClassOf(
                    OWLFunctionalSyntaxFactory.Class(clsData.getEntity().getIRI()),
                    objectSomeValuesFrom
            );
            subClassOfAxiom.accept(r);

            serializedAxes.add(writer.toString());
        }

        return serializedAxes;
    }

    public static List<String> serializeLogicalDefinitions(List<LogicalDefinition> logicalDefinitions, OWLClassData clsData) throws OWLOntologyCreationException {
        var ont = man.createOntology();
        List<String> serializedAxes = new ArrayList<String>();

        for (LogicalDefinition logDef : logicalDefinitions) {
            //not sure, if we can reuse the same writer for different axioms. If we can purge it at the end, yes. If yes, you can put this outside of the for loop
            StringWriter writer = new StringWriter();
            FunctionalSyntaxObjectRenderer r = new FunctionalSyntaxObjectRenderer(ont, writer);

            OWLClassExpression[] someValuesFromsList = new OWLClassExpression[logDef.axis2filler().size() + 1];

            someValuesFromsList[0] = logDef.logicalDefinitionParent().getEntity();

            int i = 1;
            for (PropertyClassValue axis2filler : logDef.axis2filler()) {
                var objectSomeValuesFrom = ObjectSomeValuesFrom(
                        OWLFunctionalSyntaxFactory.ObjectProperty(axis2filler.getProperty().getEntity().getIRI()),
                        OWLFunctionalSyntaxFactory.Class(axis2filler.getValue().getEntity().getIRI())
                );
                someValuesFromsList[i] = objectSomeValuesFrom;
                i++;
            }

            var intersectionOf = ObjectIntersectionOf(someValuesFromsList);

            var equivalentAx = EquivalentClasses(clsData.getEntity(), intersectionOf);
            equivalentAx.accept(r);

            serializedAxes.add(writer.toString());
        }

        return serializedAxes;
    }

}
