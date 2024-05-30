package edu.stanford.protege.webprotege.user;

import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import edu.stanford.protege.webprotege.ipc.impl.CommandExecutorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserDetailsManagerConfiguration {

    @Bean
    public CommandExecutor<UsersQueryRequest, UsersQueryResponse> getUserQueryCommandExecutor(){
        return new CommandExecutorImpl<>(UsersQueryResponse.class);
    }

}
