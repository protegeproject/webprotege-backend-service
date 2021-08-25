package edu.stanford.protege.webprotege.access;

import edu.stanford.protege.webprotege.authorization.*;
import edu.stanford.protege.webprotege.ipc.CommandExecutor;
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
        return new CommandExecutor<>(GetAssignedRolesResponse.class);
    }

    @Bean
    CommandExecutor<SetAssignedRolesRequest, SetAssignedRolesResponse> setAssignedRolesExecutor() {
        return new CommandExecutor<>(SetAssignedRolesResponse.class);
    }

    @Bean
    CommandExecutor<GetRolesRequest, GetRolesResponse> getRolesExecutor() {
        return new CommandExecutor<>(GetRolesResponse.class);
    }


    @Bean
    CommandExecutor<GetAuthorizedActionsRequest, GetAuthorizedActionsResponse> getAuthorizedActionsExecutor() {
        return new CommandExecutor<>(GetAuthorizedActionsResponse.class);
    }

    @Bean
    CommandExecutor<GetAuthorizationStatusRequest, GetAuthorizationStatusResponse> getAuthorizationStatusExecutor() {
        return new CommandExecutor<>(GetAuthorizationStatusResponse.class);
    }

    @Bean
    CommandExecutor<GetAuthorizedSubjectsRequest, GetAuthorizedSubjectsResponse> getAuthorizedSubjectsExecutor() {
        return new CommandExecutor<>(GetAuthorizedSubjectsResponse.class);
    }

    @Bean
    CommandExecutor<GetAuthorizedResourcesRequest, GetAuthorizedResourcesResponse> getAuthorizedResourcesRequest() {
        return new CommandExecutor<>(GetAuthorizedResourcesResponse.class);
    }
}
