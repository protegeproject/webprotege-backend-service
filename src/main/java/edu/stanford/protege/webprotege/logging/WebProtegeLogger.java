package edu.stanford.protege.webprotege.logging;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.user.UserId;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/03/2013
 */
public interface WebProtegeLogger {

    Marker WebProtegeMarker = MarkerFactory.getMarker("WebProtege");

    void error(Throwable t, UserId userId);

    void error(Throwable t, UserId userId, HttpServletRequest servletRequest);

    void error(Throwable t);

    void info(String message);

    void info(ProjectId projectId, String message, Object ... args);

    void info(String message, Object... args);

}
