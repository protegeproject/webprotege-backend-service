package edu.stanford.protege.webprotege.logicaldefinitions;

import edu.stanford.protege.webprotege.frame.PropertyClassValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LogicalDefinitionsDiff {
    private static final Logger logger = LoggerFactory.getLogger(LogicalDefinitionsDiff.class);

    private final List<LogicalDefinition> pristineCopy;

    private final List<LogicalDefinition> changedCopy;

    private List<LogicalDefinition> addedStatements;

    private List<LogicalDefinition> removedStatements;


    public LogicalDefinitionsDiff(List<LogicalDefinition> pristineCopy, List<LogicalDefinition> changedCopy) {
        this.pristineCopy = pristineCopy;
        this.changedCopy = changedCopy;
    }

    public void executeDiff() {
        addedStatements = diff(changedCopy, pristineCopy);
        removedStatements = diff(pristineCopy, changedCopy);
    }

    private List<LogicalDefinition> diff(List<LogicalDefinition> listOne, List<LogicalDefinition> listTwo) {

        return listOne.stream()
                .filter(two -> listTwo.stream()
                        .noneMatch(one -> one.equals(two)))
                        .collect(Collectors.toList());
    }

    public List<LogicalDefinition> getAddedStatements() {
        return Collections.unmodifiableList(addedStatements);
    }

    public List<LogicalDefinition> getRemovedStatements() {
        return Collections.unmodifiableList(removedStatements);
    }

}
