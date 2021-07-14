package edu.stanford.protege.webprotege.inject;

import com.mongodb.MongoCredential;
import com.mongodb.client.MongoDatabase;
import edu.stanford.protege.webprotege.app.WebProtegeProperties;
import edu.stanford.protege.webprotege.persistence.DbName;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class DbModule {

    
    @DbHost
    public Optional<String> provideDbHost(DbHostProvider dbHostProvider) {
        return dbHostProvider.get();
    }

    
    @DbPort
    public Optional<Integer> provideDbPort(DbPortProvider dbPortProvider) {
        return dbPortProvider.get();
    }

    
    @DbUri
    public Optional<String> provideDbUri(DbUriProvider dbUriProvider) {
        return dbUriProvider.get();
    }

    
    @DbUsername
    public String provideDbUserName(WebProtegeProperties webProtegeProperties) {
        return webProtegeProperties.getDBUserName().orElse("");
    }

    
    @DbPassword
    char [] provideDbPassword(WebProtegeProperties webProtegeProperties) {
        return webProtegeProperties.getDBPassword().orElse("").toCharArray();
    }

    
    @DbAuthenticationSource
    String provideDbAuthenticationSource(WebProtegeProperties webProtegeProperties) {
        return webProtegeProperties.getDBAuthenticationSource().orElse("");
    }

    
    public Optional<MongoCredential> provideMongoCredentials(MongoCredentialProvider credentialsProvider) {
        return credentialsProvider.get();
    }


    
    @ApplicationSingleton
    public MongoDatabase provideMongoDatabase(MongoDatabaseProvider provider) {
        return provider.get();
    }

    
    @DbName
    public String provideDbName() {
        return "webprotege";
    }
}
