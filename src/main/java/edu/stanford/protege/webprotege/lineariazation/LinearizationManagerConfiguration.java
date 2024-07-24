package edu.stanford.protege.webprotege.lineariazation;

import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import edu.stanford.protege.webprotege.ipc.impl.CommandExecutorImpl;
import org.springframework.context.annotation.*;

@Configuration
public class LinearizationManagerConfiguration {

    @Bean
    public CommandExecutor<MergeWithParentEntitiesRequest, MergeWithParentEntitiesResponse> mergeWithParentLinearizationsExecutor() {
        return new CommandExecutorImpl<>(MergeWithParentEntitiesResponse.class);
    }

}
