package edu.stanford.protege.webprotege.storage;

import edu.stanford.protege.webprotege.common.BlobLocation;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.springframework.stereotype.Component;

import jakarta.annotation.Nonnull;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-03
 */
@Component
public class MinioFileDownloader {

    private final MinioClient minioClient;

    private final MinioProperties minioProperties;

    public MinioFileDownloader(MinioClient minioClient, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }

    public CompletableFuture<Path> downloadFile(BlobLocation blobLocation) throws StorageException {
        try {
            var response = minioClient.getObject(GetObjectArgs.builder()
                                          .bucket(blobLocation.bucket())
                                          .object(blobLocation.name())
                                               .build());
            return CompletableFuture.supplyAsync(() -> {
                try {
                    var tempFile = Files.createTempFile("webprotege-", "-revision-history");
                    try(var outputStream = new BufferedOutputStream(Files.newOutputStream(tempFile))) {
                        response.transferTo(outputStream);
                    }
                    return tempFile;
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        } catch (ErrorResponseException | XmlParserException | ServerException | NoSuchAlgorithmException |
                 IOException | InvalidResponseException | InvalidKeyException | InternalException |
                 InsufficientDataException e) {
            throw new StorageException("Problem retrieving document from storage", e);
        }
    }

    public InputStream fetchDocument(@Nonnull String location) throws StorageException {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(minioProperties.getUploadsBucketName())
                    .object(location)
                    .build());
        } catch (ErrorResponseException | XmlParserException | ServerException | NoSuchAlgorithmException |
                 IOException | InvalidResponseException | InvalidKeyException | InternalException |
                 InsufficientDataException e) {
            throw new StorageException("Problem retrieving document from storage", e);
        }
    }
}
