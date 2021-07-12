package edu.stanford.protege.webprotege.api.exception;

import edu.stanford.protege.webprotege.project.UnknownProjectException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 May 2018
 */
@Provider
public class UnknownProjectExceptionMapper implements ExceptionMapper<UnknownProjectException> {

    @Context
    private UriInfo info;

    @Override
    public Response toResponse(UnknownProjectException exception) {
        return Response.status(NOT_FOUND)
                .link(info.getAbsolutePath(), "self")
                .build();
    }
}