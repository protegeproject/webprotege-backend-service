package edu.stanford.protege.webprotege.hierarchy.ordering;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.OrderedChildren;
import edu.stanford.protege.webprotege.storage.MinioFileDownloader;
import org.slf4j.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.*;

@Service
public class OrderedChildrenDocumentService {
    private final Logger logger = LoggerFactory.getLogger(OrderedChildrenDocumentService.class);

    private final MinioFileDownloader minioFileDownloader;

    private final ObjectMapper objectMapper;


    public OrderedChildrenDocumentService(MinioFileDownloader minioFileDownloader, ObjectMapper objectMapper) {
        this.minioFileDownloader = minioFileDownloader;
        this.objectMapper = objectMapper;
    }


    public Stream<OrderedChildren> fetchFromDocument(String location) {

        try {
            JsonFactory jsonFactory = new JsonFactory();
            JsonParser jsonParser = jsonFactory.createParser(minioFileDownloader.fetchDocument(location));

            if (jsonParser.nextToken() == JsonToken.START_ARRAY) {
                throw new IllegalStateException("Unexpected array");
            }

            jsonParser.nextToken();

            if (!jsonParser.getCurrentName().equals("orderedChildren") && jsonParser.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Expected the array of ordered children");
            }

            jsonParser.nextToken();

            return StreamSupport.stream(
                    new Spliterators.AbstractSpliterator<>(Long.MAX_VALUE, Spliterator.ORDERED) {
                        @Override
                        public boolean tryAdvance(Consumer<? super OrderedChildren> action) {
                            try {

                                if (jsonParser.nextToken() == JsonToken.END_ARRAY) {
                                    return false;
                                }

                                JsonNode node = objectMapper.readTree(jsonParser);
                                OrderedChildren person = objectMapper.treeToValue(node, OrderedChildren.class);
                                action.accept(person);
                                return true;
                            } catch (IOException e) {
                                throw new UncheckedIOException(e);
                            }
                        }
                    }, false);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
