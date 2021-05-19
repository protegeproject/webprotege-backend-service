package edu.stanford.protege.webprotege.app;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/01/15
 */
public interface ResourceCachingStrategy {

    boolean shouldCacheResource(String requestURI);
}
