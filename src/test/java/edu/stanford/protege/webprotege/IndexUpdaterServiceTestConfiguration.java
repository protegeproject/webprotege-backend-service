package edu.stanford.protege.webprotege;

import com.google.common.util.concurrent.MoreExecutors;
import edu.stanford.protege.webprotege.index.IndexUpdatingService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-11-03
 */
@TestConfiguration
public class IndexUpdaterServiceTestConfiguration {

    /**
     * A shell executor service that runs tasks straight away
     */
    @Primary
    @Bean
    @IndexUpdatingService
    ExecutorService indexUpdaterExecutorService() {
        return MoreExecutors.newDirectExecutorService();
    }
}
