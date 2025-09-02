package edu.stanford.protege.webprotege.events;

import edu.stanford.protege.webprotege.*;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.renderer.LiteralLexicalFormTransformer;
import jakarta.inject.Provider;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

@SpringBootTest(properties = "webprotege.rabbitmq.commands-subscribe=false")
@Import({ApplicationBeansConfiguration.class, ProjectBeansConfiguration.class, ProjectIndexBeansConfiguration.class, LuceneBeansConfiguration.class, EntityMatcherBeansConfiguration.class})
class EventTranslatorManagerLifeCycleTest {

    @org.springframework.boot.test.context.TestConfiguration
    static class TestConfiguration {
        @Bean
        ProjectId projectId() {
            return ProjectId.generate();
        }
        @Bean
        edu.stanford.protege.webprotege.renderer.LiteralRenderer literalRenderer() {
            // Minimal test impl; avoids pulling the whole dependency graph
            return new edu.stanford.protege.webprotege.owlapi.StringFormatterLiteralRendererImpl(
                    new ShortFormProvider() {
                        @NotNull
                        @Override
                        public String getShortForm(@NotNull OWLEntity owlEntity) {
                            return owlEntity.getIRI().toString();
                        }

                        @Override
                        public void dispose() {

                        }
                    }
                    , new LiteralLexicalFormTransformer() {
                @NotNull
                @Override
                public String transformLexicalForm(@NotNull String lexicalForm) {
                    return lexicalForm;
                }
            });
        }
    }

    @Autowired
    private Provider<EventTranslatorManager> managerProvider;

    @Test
    public void shouldAllowMultipleInstancesWithoutSharedDependencies() {
        // Calling the prepareForOntologyChanges method should not interact with shared deps.
        // If there is problems then the session checker would raise an exception
        var changes = List.<OntologyChange>of();

        var managerA = managerProvider.get();
        var sessionIdA = EventTranslatorSessionId.create();
        managerA.prepareForOntologyChanges(sessionIdA, changes);

        var managerB = managerProvider.get();
        var sessionIdB = EventTranslatorSessionId.create();
        managerB.prepareForOntologyChanges(sessionIdB, changes);
    }
}