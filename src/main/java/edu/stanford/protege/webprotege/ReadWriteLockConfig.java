package edu.stanford.protege.webprotege;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class ReadWriteLockConfig {

    @Value("${webprotege.readWriteLock.maxRetries:5}")
    private int maxRetries;

    @Value("${webprotege.readWriteLock.timeoutInMillies:1000}")
    private int timeout;

    private final TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    public int getMaxRetries() {
        return maxRetries;
    }

    public int getTimeout() {
        return timeout;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
}
