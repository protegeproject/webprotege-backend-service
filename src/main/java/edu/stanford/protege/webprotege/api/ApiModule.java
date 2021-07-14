package edu.stanford.protege.webprotege.api;

import edu.stanford.protege.webprotege.api.exception.PermissionDeniedExceptionMapper;
import edu.stanford.protege.webprotege.api.exception.UnknownProjectExceptionMapper;
import edu.stanford.protege.webprotege.api.resources.ProjectsResource;
import edu.stanford.protege.webprotege.api.resources.RpcResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Apr 2018
 */
public class ApiModule {

    
    public ServletContainer provideServletContainer(ResourceConfig resourceConfig) {
        return new ServletContainer(resourceConfig);
    }

    
    public ResourceConfig provideResourceConfig(ApiKeyManager apiKeyManager,
                                                Set<ApiRootResource> apiRootResources) {
        ResourceConfig resourceConfig = new ResourceConfig();

        // A filter to ensure that either a session token (with an associated user) is
        // provided or an api key (also associated with a user) is provided
        resourceConfig.register(new AuthenticationFilter(apiKeyManager));

        // Custom injection into the context of the user id identified by the session
        // token or the api key
        resourceConfig.register(new UserIdBinder());
        resourceConfig.register(new ApiKeyBinder());
        resourceConfig.register(new ExecutionContextBinder());

        resourceConfig.register(new JacksonContextResolver());

        // Exception mappers
        resourceConfig.register(new PermissionDeniedExceptionMapper());
        resourceConfig.register(new UnknownProjectExceptionMapper());

        // Add injected resources
        apiRootResources.forEach(resourceConfig::register);

        return resourceConfig;
    }

    
    ApiRootResource provideProjectsResource(ProjectsResource resource) {
        return resource;
    }

    
    ApiRootResource provideRpcResource(RpcResource resource) {
        return resource;
    }



}
