package edu.stanford.protege.webprotege.access;

import edu.stanford.protege.webprotege.authorization.*;
import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import edu.stanford.protege.webprotege.ipc.impl.CommandExecutorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-10
 */
@Configuration
public class AccessManagerConfiguration {

    @Bean
    CommandExecutor<GetAssignedRolesRequest, GetAssignedRolesResponse> assignedRolesExecutor() {
        return new CommandExecutorImpl<>(GetAssignedRolesResponse.class);
    }

    @Bean
    CommandExecutor<SetAssignedRolesRequest, SetAssignedRolesResponse> setAssignedRolesExecutor() {
        return new CommandExecutorImpl<>(SetAssignedRolesResponse.class);
    }

    @Bean
    CommandExecutor<GetRolesRequest, GetRolesResponse> getRolesExecutor() {
        return new CommandExecutorImpl<>(GetRolesResponse.class);
    }

    @Bean
    CommandExecutor<GetAuthorizedActionsRequest, GetAuthorizedActionsResponse> getAuthorizedActionsExecutor() {
        return new CommandExecutorImpl<>(GetAuthorizedActionsResponse.class);
    }

    @Bean
    CommandExecutor<GetAuthorizedSubjectsRequest, GetAuthorizedSubjectsResponse> getAuthorizedSubjectsExecutor() {
        return new CommandExecutorImpl<>(GetAuthorizedSubjectsResponse.class);
    }

    @Bean
    CommandExecutor<GetAuthorizedResourcesRequest, GetAuthorizedResourcesResponse> getAuthorizedResourcesRequest() {
        return new CommandExecutorImpl<>(GetAuthorizedResourcesResponse.class);
    }
}
