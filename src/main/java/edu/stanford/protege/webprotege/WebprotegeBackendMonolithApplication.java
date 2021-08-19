package edu.stanford.protege.webprotege;

import edu.stanford.protege.webprotege.ipc.WebProtegeIpcApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(WebProtegeIpcApplication.class)
public class WebprotegeBackendMonolithApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebprotegeBackendMonolithApplication.class, args);
	}

}
