package org.finance.business.web;

import io.swagger.models.Swagger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.service.Documentation;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;
import springfox.documentation.swagger2.web.Swagger2ControllerWebMvc;
import springfox.documentation.swagger2.web.WebMvcSwaggerTransformationFilter;

import javax.servlet.http.HttpServletRequest;

import static java.util.Optional.ofNullable;

/**
 * @author jiangbangfa
 */
@Controller
@Component("openapiWeb")
public class OpenapiWeb {

    private static final Logger LOGGER = LoggerFactory.getLogger(Swagger2ControllerWebMvc.class);
    private static final String HAL_MEDIA_TYPE = "application/hal+json";
    private final DocumentationCache documentationCache;
    private final ServiceModelToSwagger2Mapper mapper;
    private final JsonSerializer jsonSerializer;
    private final PluginRegistry<WebMvcSwaggerTransformationFilter, DocumentationType> transformations;

    @Autowired
    public OpenapiWeb(
            DocumentationCache documentationCache,
            ServiceModelToSwagger2Mapper mapper,
            JsonSerializer jsonSerializer,
            @Qualifier("webMvcSwaggerTransformationFilterRegistry")
                    PluginRegistry<WebMvcSwaggerTransformationFilter, DocumentationType> transformations) {
        this.documentationCache = documentationCache;
        this.mapper = mapper;
        this.jsonSerializer = jsonSerializer;
        this.transformations = transformations;
    }


    @GetMapping("/api/downloadOpenapi")
    public ResponseEntity<Json> getDocumentation(HttpServletRequest request) {
        String groupName = ofNullable("default").orElse(Docket.DEFAULT_GROUP_NAME);
        Documentation documentation = documentationCache.documentationByGroup(groupName);
        if (documentation == null) {
            LOGGER.warn("Unable to find specification for group {}", groupName);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Swagger swagger = mapper.mapDocumentation(documentation);
        return new ResponseEntity<>(jsonSerializer.toJson(swagger), HttpStatus.OK);
    }

}
