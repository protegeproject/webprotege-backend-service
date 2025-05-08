package edu.stanford.protege.webprotege.access;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import edu.stanford.protege.webprotege.authorization.Capability;
import edu.stanford.protege.webprotege.forms.FormRegionCapability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ObjectMapperExtraCapabilitiesCustomizer {

    private final static Logger logger = LoggerFactory.getLogger(ObjectMapperExtraCapabilitiesCustomizer.class);

    private final ObjectMapper objectMapper;

    public ObjectMapperExtraCapabilitiesCustomizer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void customize() throws JsonProcessingException {
        logger.info("Registering FormRegionCapability on object mapper");
        objectMapper.registerSubtypes(new NamedType(FormRegionCapability.class, FormRegionCapability.TYPE));
    }

}
