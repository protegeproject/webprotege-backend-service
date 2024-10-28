package edu.stanford.protege.webprotege.postcoordination;

import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import edu.stanford.protege.webprotege.ipc.impl.CommandExecutorImpl;
import org.springframework.context.annotation.*;

@Configuration
public class PostcoordinationManagerConfiguration {

    @Bean
    public CommandExecutor<CreatePostcoordinationFromParentRequest, CreatePostcoordinationFromParentResponse> createPostcoordinationFromParentExecutor() {
        return new CommandExecutorImpl<>(CreatePostcoordinationFromParentResponse.class);
    }

}
