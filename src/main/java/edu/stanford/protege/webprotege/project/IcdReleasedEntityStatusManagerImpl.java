package edu.stanford.protege.webprotege.project;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;



public class IcdReleasedEntityStatusManagerImpl implements IcdReleasedEntityStatusManager {

    @Value("${webprotege.releasedClassesUrl}")
    private String ICD_RELEASED_URL;

    private final OkHttpClient httpClient = new OkHttpClient();

    private final Logger logger = LoggerFactory.getLogger(IcdReleasedEntityStatusManagerImpl.class);


    public IcdReleasedEntityStatusManagerImpl(){}

    @Override
    public List<String> fetchIris() {
        List<String> iris = new ArrayList<>();
        Request request = new Request.Builder()
                .url(ICD_RELEASED_URL)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                Arrays.stream(responseBody.split("\n")).forEach(line -> iris.add(line.trim()));
                for (String line : responseBody.split("\n")) {
                    iris.add(line.trim());
                }
            } else {
                logger.error("GET ICD released classes request did not worked, Response Code: {}", response.code());
                return Collections.emptyList();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return iris;
    }
}
