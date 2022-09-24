package org.finance;

import org.finance.business.entity.Subject;
import org.finance.business.mapper.SubjectMapper;
import org.finance.infrastructure.util.CollectionUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
        List<Subject> dbSubjects = subjectMapper.selectList(null);
        Map<Long, List<Subject>> subjectsByIndustryId = dbSubjects.stream().collect(Collectors.groupingBy(Subject::getIndustryId));

        Set<Map.Entry<Long, List<Subject>>> entries = subjectsByIndustryId.entrySet();
        for (Map.Entry<Long, List<Subject>> entry : entries) {
            Long industryId = entry.getKey();
            Map<Long, List<Subject>> subjectsByCustomerId = entry.getValue().stream().collect(Collectors.groupingBy(Subject::getCustomerId));
            Set<Map.Entry<Long, List<Subject>>> entry2 = subjectsByCustomerId.entrySet();
            for (Map.Entry<Long, List<Subject>> entry3 : entry2) {
                List<Subject> subjects = entry3.getValue();
                List<Subject> treeSubject = CollectionUtil.transformTree(subjects, Subject::getNumber, sub -> {
                    String number = sub.getNumber();
                    int length = number.length();
                    if (industryId == 1 || industryId == 3) {
                        if (length == 3) {
                            return "0";
                        }
                        return number.substring(0, length - 2);
                    } else if (length == 4) {
                        return "0";
                    }
                    return number.substring(0, length - 2);
                }, Subject::getChildren, Subject::setChildren);
                recursion(treeSubject, null, null, null);
                subjects.forEach(subjectMapper::updateById);
            }
            System.out.println("test");
        }
    }

    public void recursion(List<Subject> list, Subject parent, AtomicInteger counter, AtomicInteger levelCounter) {
        if (list == null || list.isEmpty()) {
            return;
        }
        boolean isFirstRound = counter == null;
        for (Subject subject : list) {
            counter = isFirstRound ? new AtomicInteger(1) : counter;
            subject.setLeftValue(counter.getAndIncrement());
            subject.setParentId(parent == null ? 0 : parent.getId());
            subject.setParentNumber(parent == null ? "0" : parent.getNumber());
            subject.setRootNumber(parent == null ? subject.getNumber() : parent.getRootNumber());
            subject.setLevel(levelCounter == null ? 1 : levelCounter.get());
            subject.setHasLeaf(!CollectionUtils.isEmpty(subject.getChildren()));
            recursion(subject.getChildren(), subject, counter, levelCounter == null ? new AtomicInteger(2) : new AtomicInteger(levelCounter.get() + 1));
            subject.setRightValue(counter.getAndIncrement());
        }
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
