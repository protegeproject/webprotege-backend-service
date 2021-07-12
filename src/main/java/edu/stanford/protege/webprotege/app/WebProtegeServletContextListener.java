package edu.stanford.protege.webprotege.app;

import ch.qos.logback.classic.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import static edu.stanford.protege.webprotege.logging.WebProtegeLogger.WebProtegeMarker;

public class WebProtegeServletContextListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(WebProtegeServletContextListener.class);

    public WebProtegeServletContextListener() {
    }

    public void contextInitialized(ServletContextEvent sce) {
        logger.info(WebProtegeMarker, "Initializing WebProtege");
//
//        ServerComponent serverComponent = DaggerServerComponent.create();
//
//        ServletContext servletContext = sce.getServletContext();
//
//        servletContext.setAttribute(ServerComponent.class.getName(), serverComponent);
//
//        servletContext.addServlet("ProjectDownloadServlet", serverComponent.getProjectDownloadServlet())
//                      .addMapping("/download");
//
//        servletContext.addServlet("FileUploadServlet", serverComponent.getFileUploadServlet())
//                      .addMapping("/webprotege/submitfile");
//
//        servletContext.addServlet("JerseyContainerServlet", serverComponent.getJerseyServletContainer())
//                      .addMapping("/data/*");
//
//        servletContext.addServlet("JerseyContainerServlet_Rpc", serverComponent.getJerseyServletContainer())
//                      .addMapping("/api/*");
//
//        servletContext.addServlet("AuthenticationServlet", serverComponent.getAuthenticationServlet())
//                      .addMapping("/authenticate");
//
//        servletContext.addListener(serverComponent.getSessionListener());
//        serverComponent.getWebProtegeConfigurationChecker().performConfiguration();
//        serverComponent.getProjectCacheManager().start();

        Runtime runtime = Runtime.getRuntime();
        logger.info("Max  Memory: {} MB", (runtime.maxMemory() / (1024 * 1024)));
        logger.info(WebProtegeMarker, "WebProtege initialization complete");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        try {
            logger.info(WebProtegeMarker, "Shutting down WebProtege");
            var servletContext = servletContextEvent.getServletContext();
            var serverComponent = (ServerComponent) servletContext.getAttribute(ServerComponent.class.getName());
            if (serverComponent != null) {
                logger.info(WebProtegeMarker, "Disposing of objects");
                serverComponent.getApplicationDisposablesManager().dispose();
            }
            logger.info(WebProtegeMarker, "WebProtege shutdown complete");
            // Finally stop logging
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            loggerContext.stop();
        } finally {
            var servletContext = servletContextEvent.getServletContext();
            servletContext.removeAttribute(ServerComponent.class.getName());
        }

    }
}
