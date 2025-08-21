package edu.stanford.protege.webprotege;

import edu.stanford.protege.webprotege.ipc.EventHandler;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import edu.stanford.protege.webprotege.permissions.PermissionsChangedEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebProtegeHandler
public class TestHandler implements EventHandler<PermissionsChangedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(TestHandler.class);

    @NotNull
    @Override
    public String getChannelName() {
        return PermissionsChangedEvent.CHANNEL;
    }

    @NotNull
    @Override
    public String getHandlerName() {
        return "TestPermissionChangedHandler";
    }

    @Override
    public Class<PermissionsChangedEvent> getEventClass() {
        return PermissionsChangedEvent.class;
    }

    @Override
    public void handleEvent(PermissionsChangedEvent event) {
        logger.info("*** Handling permissions changed event: {}", event);
    }
}
