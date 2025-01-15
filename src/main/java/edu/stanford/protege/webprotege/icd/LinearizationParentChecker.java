package edu.stanford.protege.webprotege.icd;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.linearization.LinearizationManager;
import org.semanticweb.owlapi.model.IRI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Component
public class LinearizationParentChecker {

    private final Logger logger = LoggerFactory.getLogger(LinearizationParentChecker.class);


    @Nonnull
    private final LinearizationManager linearizationManager;

    @Nonnull
    private final ProjectId projectId;

    public LinearizationParentChecker(LinearizationManager linearizationManager, @Nonnull ProjectId projectId) {
        this.linearizationManager = linearizationManager;
        this.projectId = projectId;
    }

    public Optional<IRI> getParentThatIsLinearizationPathParent(IRI owlClas, Set<IRI> parentClasses) {
        try {
            return linearizationManager.getParentThatIsLinearizationPathParent(owlClas, parentClasses, projectId, new ExecutionContext()).get();
        } catch (InterruptedException | ExecutionException e) {
            String message = MessageFormat.format("Could not check if any parents are linearization path parents for {0}", owlClas.toString());
            logger.error(message, e);
            throw new RuntimeException(message, e);
        }
    }
}
