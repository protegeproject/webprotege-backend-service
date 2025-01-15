package edu.stanford.protege.webprotege.linearization;

import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import edu.stanford.protege.webprotege.ipc.impl.CommandExecutorImpl;
import org.springframework.context.annotation.*;

@Configuration
public class LinearizationManagerConfiguration {

    @Bean
    public CommandExecutor<MergeWithParentEntitiesRequest, MergeWithParentEntitiesResponse> mergeWithParentLinearizationsExecutor() {
        return new CommandExecutorImpl<>(MergeWithParentEntitiesResponse.class);
    }

    @Bean
    public CommandExecutor<CreateLinearizationFromParentRequest, CreateLinearizationFromParentResponse> createLinearizationFromParentExecutor() {
        return new CommandExecutorImpl<>(CreateLinearizationFromParentResponse.class);
    }

    @Bean
    public CommandExecutor<GetParentThatIsLinearizationPathParentRequest, GetParentThatIsLinearizationPathParentResponse> isAnyParentLinearizationParentExecutor() {
        return new CommandExecutorImpl<>(GetParentThatIsLinearizationPathParentResponse.class);
    }

}
