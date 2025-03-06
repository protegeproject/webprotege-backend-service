package edu.stanford.protege.webprotege.card;

import com.google.common.collect.ImmutableList;
import com.mongodb.client.MongoCollection;
import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.color.Color;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.criteria.CompositeRootCriteria;
import edu.stanford.protege.webprotege.criteria.MultiMatchType;
import edu.stanford.protege.webprotege.forms.FormId;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "webprotege.rabbitmq.commands-subscribe=false")
@ExtendWith(MongoTestExtension.class)
@ActiveProfiles("test")
class CardDescriptorRepositoryImpl_IT {

    private static final ProjectId projectId = ProjectId.generate();

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CardDescriptorRepositoryImpl repository;

    private CardDescriptor cardDescriptor;

    @BeforeEach
    void setUp() {

        cardDescriptor = CardDescriptor.create(CardId.valueOf("78a342b8-e4ce-4ed0-ae4f-1cefb4e2d49f"),
                LanguageMap.empty(),
                Color.getWhite(),
                Color.getWhite(),
                FormCardContentDescriptor.create(FormId.get("cb0455cd-8c55-4029-ac30-453c3619f803")),
                Set.of(),
                Set.of(),
                CompositeRootCriteria.get(ImmutableList.of(), MultiMatchType.ALL));
    }

    @AfterEach
    void tearDown() {
        getCollection().drop();
    }

    @Test
    void shouldNotFindAny() {
        var descriptors = repository.getCardDescriptors(projectId);
        assertThat(descriptors).isEmpty();
    }

    @Test
    void shouldSaveRecord() {
        repository.setCardDescriptors(projectId, List.of(cardDescriptor));
        var collection = getCollection();
        assertThat(collection.countDocuments()).isEqualTo(1);
    }

    @Test
    void shouldNotSaveDuplicateRecords() {
        repository.setCardDescriptors(projectId, List.of(cardDescriptor));
        repository.setCardDescriptors(projectId, List.of(cardDescriptor));
        var collection = getCollection();
        assertThat(collection.countDocuments()).isEqualTo(1);
    }

    @Test
    void shouldRoundTripSavedRecord() {
        repository.setCardDescriptors(projectId, List.of(cardDescriptor));
        var found = repository.getCardDescriptors(projectId);
        assertThat(found).hasSize(1);
        assertThat(found).contains(cardDescriptor);
    }

    @Test
    void shouldClearRecord() {
        repository.setCardDescriptors(projectId, List.of(cardDescriptor));

        var collection = getCollection();
        assertThat(collection.countDocuments()).isEqualTo(1);

        repository.clearCardDescriptors(projectId);
        assertThat(collection.countDocuments()).isEqualTo(0);
    }

    private @NotNull MongoCollection<Document> getCollection() {
        var collection = mongoTemplate.getCollection("CardDescriptors");
        return collection;
    }
}