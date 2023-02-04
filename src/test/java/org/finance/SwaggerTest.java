package org.finance;

import org.finance.business.entity.Subject;
import org.finance.business.mapper.SubjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    @Resource
    private SubjectMapper subjectMapper;

    @Test
    public void test() {
    }

    public void recursion(List<Subject> list, Subject parent, AtomicInteger counter, AtomicInteger levelCounter) {
    }

    /*private MockMvc mockMvc;

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
    }*/

}
