package edu.stanford.protege.webprotege.hierarchy.ordering;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.OrderedChildren;
import edu.stanford.protege.webprotege.storage.MinioFileDownloader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderedChildrenDocumentServiceTest {

    @Mock
    private MinioFileDownloader minioFileDownloader;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private OrderedChildrenDocumentService orderedChildrenDocumentService;

    private static final String ORDERED_SIBLINGS_TEST_JSON = "src/test/resources/orderedSiblingsTest.json";

    @BeforeEach
    void setUp() throws IOException {
        File testFile = new File(ORDERED_SIBLINGS_TEST_JSON);
        InputStream testFileInputStream = new FileInputStream(testFile);
        when(minioFileDownloader.fetchDocument(anyString())).thenReturn(testFileInputStream);
        orderedChildrenDocumentService = new OrderedChildrenDocumentService(minioFileDownloader, objectMapper);
    }

    @Test
    void GIVEN_validJson_WHEN_fetchFromDocument_THEN_returnOrderedChildrenStream() {
        Stream<OrderedChildren> orderedChildrenStream = orderedChildrenDocumentService.fetchFromDocument("some-location");

        List<OrderedChildren> orderedChildrenList = orderedChildrenStream.collect(Collectors.toList());

        assertNotNull(orderedChildrenList);
        assertEquals(2, orderedChildrenList.size());

        OrderedChildren firstEntry = orderedChildrenList.get(0);
        assertNotNull(firstEntry);
        assertEquals("http://id.who.int/icd/entity/1439203351", firstEntry.entityUri());
        assertTrue(firstEntry.orderedChildren().isEmpty());

        OrderedChildren secondEntry = orderedChildrenList.get(1);
        assertNotNull(secondEntry);
        assertEquals("http://id.who.int/icd/entity/360081115", secondEntry.entityUri());
        assertEquals(15, secondEntry.orderedChildren().size());
    }
}
