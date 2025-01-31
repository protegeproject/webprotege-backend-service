package edu.stanford.protege.webprotege.icd;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.linearization.LinearizationManager;
import org.semanticweb.owlapi.model.IRI;
import org.slf4j.*;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Component
public class LinearizationParentChecker {

    private final Logger logger = LoggerFactory.getLogger(LinearizationParentChecker.class);


    @Nonnull
    private final LinearizationManager linearizationManager;

    public LinearizationParentChecker(LinearizationManager linearizationManager) {
        this.linearizationManager = linearizationManager;
    }

    public Set<IRI> getParentThatIsLinearizationPathParent(IRI owlClas, Set<IRI> parentClasses, ProjectId projectId) {
        try {
            return linearizationManager.getParentsThatAreLinearizationPathParents(owlClas, parentClasses, projectId, new ExecutionContext()).get();
        } catch (InterruptedException | ExecutionException e) {
            String message = MessageFormat.format("Could not check if any parents are linearization path parents for {0}", owlClas.toString());
            logger.error(message, e);
            throw new RuntimeException(message, e);
        }
    }
}
