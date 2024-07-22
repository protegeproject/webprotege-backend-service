package edu.stanford.protege.webprotege.linearization;

import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import edu.stanford.protege.webprotege.ipc.impl.CommandExecutorImpl;
import org.springframework.context.annotation.*;

@Configuration
public class LinearizationManagersConfiguration {

    @Bean
    public CommandExecutor<CreateLinearizationFromParentRequest, CreateLinearizationFromParentResponse> createLinearizationFromParentExecutor() {
        return new CommandExecutorImpl<>(CreateLinearizationFromParentResponse.class);
    }

}
