package org.finance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.io.BufferedOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author jiangbangfa
 */
@WebAppConfiguration
@SpringBootTest
public class SwaggerTest {

    private static final String SWAGGER_JSON_HREF = "/v2/api-docs";

    private static final String SWAGGER_JSON_OUT_DIRECTORY = "src/main/resources/static/swagger.json";

    @Resource
    private WebApplicationContext applicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.applicationContext).build();
    }

    @Test
    public void createSwaggerJson() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(SWAGGER_JSON_HREF)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        MockHttpServletResponse servletResponse = mvcResult.getResponse();
        String swaggerJson = servletResponse.getContentAsString(StandardCharsets.UTF_8);
        Path swaggerPath = Paths.get(SWAGGER_JSON_OUT_DIRECTORY);
        if(Files.notExists(swaggerPath.getParent()) || !Files.isDirectory(swaggerPath.getParent())) {
            Files.createDirectories(swaggerPath.getParent());
        }

        try (BufferedOutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(swaggerPath))){
            StreamUtils.copy(swaggerJson, StandardCharsets.UTF_8, outputStream);
        }
    }

}
