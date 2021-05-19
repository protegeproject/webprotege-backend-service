package edu.stanford.protege.webprotege.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.session.WebProtegeSessionImpl;
import edu.stanford.protege.webprotege.dispatch.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-14
 */
public class AuthenticationServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServlet.class);

    private final ObjectMapper objectMapper;

    private final ActionExecutor actionExecutor;

    @Inject
    public AuthenticationServlet(ObjectMapper objectMapper,
                                 ActionExecutor actionExecutor) {
        this.objectMapper = checkNotNull(objectMapper);
        this.actionExecutor = actionExecutor;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var action = objectMapper.readValue(req.getInputStream(), Action.class);
            if(!(action instanceof PerformLoginAction)) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            var performLoginAction = (PerformLoginAction) action;
            var performLoginResult = actionExecutor.execute(performLoginAction, new ExecutionContext(
                    new WebProtegeSessionImpl(req.getSession())
            ));
            var authResponse = performLoginResult.getAuthenticationResponse();
            if(authResponse == AuthenticationResponse.SUCCESS) {
                // Create a session and this will set the session cookie
                var session = req.getSession(true);
                resp.setStatus(HttpServletResponse.SC_OK);
                logger.info("User logged in with Session Id {}", session.getId());
            }
            else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            objectMapper.writeValue(resp.getOutputStream(), performLoginResult);
        } catch (IOException e) {
            logger.info("Bad login request", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

    }
}
