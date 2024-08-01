package edu.stanford.protege.webprotege.forms;

import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

@SpringBootApplication
//@Import(WebProtegeJacksonApplication.class)
public class WebprotegeFormsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebprotegeFormsApiApplication.class, args);
    }

    @Bean
    @ConditionalOnMissingBean
    OWLDataFactory dataFactory() {
        return new OWLDataFactoryImpl();
    }
}
