package edu.stanford.protege.webprotege.logicaldefinitions;

import edu.stanford.protege.webprotege.entity.OWLClassData;
import edu.stanford.protege.webprotege.frame.PropertyClassValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class LogicalDefinitionsDiff {
    private static final Logger logger = LoggerFactory.getLogger(LogicalDefinitionsDiff.class);

    private final List<LogicalDefinition> pristineCopy;

    private final List<LogicalDefinition> changedCopy;

    private Map<OWLClassData, Set<PropertyClassValue>> addedStatementsMap = new HashMap<>();
    private Map<OWLClassData, Set<PropertyClassValue>> removedStatementsMap = new HashMap<>();


    public LogicalDefinitionsDiff(List<LogicalDefinition> pristineCopy, List<LogicalDefinition> changedCopy) {
        this.pristineCopy = pristineCopy;
        this.changedCopy = changedCopy;
    }

    public void executeDiff() {

        for(LogicalDefinition changed : changedCopy) {
            Optional<LogicalDefinition> existingParent = pristineCopy.stream().filter(c -> c.logicalDefinitionParent().equals(changed.logicalDefinitionParent())).findFirst();
            if(existingParent.isEmpty()) {
                addedStatementsMap.put(changed.logicalDefinitionParent(), new HashSet<>(changed.axis2filler()));
            } else {
                addedStatementsMap.put(changed.logicalDefinitionParent(), getDiffElements(changed.axis2filler(), existingParent.get().axis2filler()));
            }
        }

        for(LogicalDefinition changed : pristineCopy) {
            Optional<LogicalDefinition> existingParent = changedCopy.stream().filter(c -> c.logicalDefinitionParent().equals(changed.logicalDefinitionParent())).findFirst();

            if(existingParent.isEmpty()) {
                removedStatementsMap.put(changed.logicalDefinitionParent(), new HashSet<>(changed.axis2filler()));
            } else {
                removedStatementsMap.put(changed.logicalDefinitionParent(), getDiffElements(changed.axis2filler(), existingParent.get().axis2filler()));
            }
        }


    }

    private Set<PropertyClassValue> getDiffElements(List<PropertyClassValue> extraElements, List<PropertyClassValue> originalElements) {
        return extraElements.stream().filter(e -> !originalElements.contains(e)).collect(Collectors.toSet());
    }


    private List<LogicalDefinition> diff(List<LogicalDefinition> listOne, List<LogicalDefinition> listTwo) {

        return listOne.stream()
                .filter(two -> listTwo.stream()
                        .noneMatch(one -> one.equals(two)))
                        .collect(Collectors.toList());
    }

    public Map<OWLClassData, Set<PropertyClassValue>> getAddedStatementsMap() {
        return addedStatementsMap;
    }

    public Map<OWLClassData, Set<PropertyClassValue>> getRemovedStatementsMap() {
        return removedStatementsMap;
    }
}
