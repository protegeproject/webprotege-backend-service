package edu.stanford.protege.webprotege.logicaldefinitions;

import edu.stanford.protege.webprotege.frame.PropertyClassValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class NecessaryConditionsDiff {
    private static final Logger logger = LoggerFactory.getLogger(NecessaryConditionsDiff.class);

    private final List<PropertyClassValue> pristineCopy;

    private final List<PropertyClassValue> changedCopy;

    private List<PropertyClassValue> addedStatements;

    private List<PropertyClassValue> removedStatements;


    public NecessaryConditionsDiff(List<PropertyClassValue> pristineCopy, List<PropertyClassValue> changedCopy) {
        this.pristineCopy = pristineCopy;
        this.changedCopy = changedCopy;
    }

    public void executeDiff() {
        addedStatements = diff(changedCopy, pristineCopy);
        removedStatements = diff(pristineCopy, changedCopy);
    }
    
    private List<PropertyClassValue> diff(List<PropertyClassValue> listOne, List<PropertyClassValue> listTwo) {
       return listOne.stream()
                .filter(two -> listTwo.stream().
                        noneMatch(one -> one.getProperty().equals(two.getProperty()) &&
                                one.getValue().equals(two.getValue())))
                .collect(Collectors.toList());
    }

    public List<PropertyClassValue> getAddedStatements() {
        return Collections.unmodifiableList(addedStatements);
    }

    public List<PropertyClassValue> getRemovedStatements() {
        return Collections.unmodifiableList(removedStatements);
    }
}
