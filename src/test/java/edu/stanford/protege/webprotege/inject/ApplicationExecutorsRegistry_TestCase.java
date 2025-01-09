package edu.stanford.protege.webprotege.inject;

import edu.stanford.protege.webprotege.app.ApplicationDisposablesManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.concurrent.ExecutorService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ApplicationExecutorsRegistry_TestCase {

    private ApplicationExecutorsRegistry registry;

    @Mock
    private ApplicationDisposablesManager applicationDisposablesManager;

    @Mock
    private ExecutorService executorService;

    private final String serviceName = "The service name";

    @BeforeEach
    public void setUp() throws Exception {
        registry = new ApplicationExecutorsRegistry(applicationDisposablesManager);
    }

    @Test
    public void shouldRegisterService() {
        registry.registerService(executorService, serviceName);
        var shutdownTask = ArgumentCaptor.forClass(ExecutorServiceShutdownTask.class);
        verify(applicationDisposablesManager, times(1)).register(shutdownTask.capture());
        var service = shutdownTask.getValue().getExecutorService();
        assertThat(service, is(executorService));
    }

    @Test
    public void shouldNotRegisterMultipleTimes() {
        registry.registerService(executorService, serviceName);
        registry.registerService(executorService, serviceName);
        verify(applicationDisposablesManager, times(1)).register(any());
    }

    /** @noinspection ConstantConditions*/
    @Test
public void shouldThrowNpeIfServiceIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        registry.registerService(null, serviceName);
     });
}

    /** @noinspection ConstantConditions*/
    @Test
public void shouldThrowNpeIfServiceNameIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        registry.registerService(executorService, null);
     });
}
}