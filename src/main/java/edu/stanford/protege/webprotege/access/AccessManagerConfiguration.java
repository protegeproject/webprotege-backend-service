package edu.stanford.protege.webprotege.access;

import edu.stanford.protege.webprotege.authorization.*;
import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import edu.stanford.protege.webprotege.ipc.pulsar.PulsarCommandExecutor;
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
        return new PulsarCommandExecutor<>(GetAssignedRolesResponse.class);
    }

    @Bean
    CommandExecutor<SetAssignedRolesRequest, SetAssignedRolesResponse> setAssignedRolesExecutor() {
        return new PulsarCommandExecutor<>(SetAssignedRolesResponse.class);
    }

    @Bean
    CommandExecutor<GetRolesRequest, GetRolesResponse> getRolesExecutor() {
        return new PulsarCommandExecutor<>(GetRolesResponse.class);
    }

    @Bean
    CommandExecutor<GetAuthorizedActionsRequest, GetAuthorizedActionsResponse> getAuthorizedActionsExecutor() {
        return new PulsarCommandExecutor<>(GetAuthorizedActionsResponse.class);
    }

    @Bean
    CommandExecutor<GetAuthorizedSubjectsRequest, GetAuthorizedSubjectsResponse> getAuthorizedSubjectsExecutor() {
        return new PulsarCommandExecutor<>(GetAuthorizedSubjectsResponse.class);
    }

    @Bean
    CommandExecutor<GetAuthorizedResourcesRequest, GetAuthorizedResourcesResponse> getAuthorizedResourcesRequest() {
        return new PulsarCommandExecutor<>(GetAuthorizedResourcesResponse.class);
    }
}
