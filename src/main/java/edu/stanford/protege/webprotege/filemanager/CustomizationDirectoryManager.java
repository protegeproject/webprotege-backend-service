package edu.stanford.protege.webprotege.filemanager;

import com.google.common.io.BaseEncoding;
import edu.stanford.protege.webprotege.common.UserId;

import javax.inject.Inject;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/16
 */
public class CustomizationDirectoryManager {

    private static final String DEFAULT_DIRECTORY_NAME = "default";

    private static final String USER_PREFIX = "user-";

    private final File baseDirectory;

    @Inject
    public CustomizationDirectoryManager(File baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    public File getDefaultDirectory() {
        return new File(baseDirectory, DEFAULT_DIRECTORY_NAME);
    }

    public File getUserDirectory(UserId userId) {
        String hashedUserId = getHashedUserId(userId);
        return new File(baseDirectory, USER_PREFIX + hashedUserId);
    }


    private String getHashedUserId(UserId userId) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte [] digest = messageDigest.digest(userId.id().getBytes(StandardCharsets.UTF_8));
            return BaseEncoding.base16().lowerCase().encode(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
