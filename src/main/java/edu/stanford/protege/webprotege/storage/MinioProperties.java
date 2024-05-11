package edu.stanford.protege.webprotege.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-03
 */
@Component
@ConfigurationProperties(prefix = "webprotege.minio")
public class MinioProperties {

    private String accessKey;

    private String secretKey;

    private String endPoint;

    private String revisionHistoryDocumentsBucketName;

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public String getRevisionHistoryDocumentsBucketName() {
        return revisionHistoryDocumentsBucketName;
    }

    public void setRevisionHistoryDocumentsBucketName(String revisionHistoryDocumentsBucketName) {
        this.revisionHistoryDocumentsBucketName = revisionHistoryDocumentsBucketName;
    }
}
