package com.rahulchauhan.devfolio.project;

import com.rahulchauhan.devfolio.TestcontainersConfig;
import com.rahulchauhan.devfolio.skill.Skill;
import com.rahulchauhan.devfolio.skill.SkillCategory;
import com.rahulchauhan.devfolio.skill.SkillRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfig.class)
@Transactional
public class ProjectApiIntegrationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void setUp() {
        Skill java = new Skill("Java", SkillCategory.LANGUAGE);
        java.setProficiency((short) 4);
        skillRepository.saveAndFlush(java);

        Skill spring = new Skill("Spring Boot", SkillCategory.BACKEND);
        spring.setProficiency((short) 4);
        skillRepository.saveAndFlush(spring);

        Project publishedProject = new Project("published-app", "Published App", "Published project", ProjectStatus.PUBLISHED);
        publishedProject.addSkill(java);
        publishedProject.addSkill(spring);
        projectRepository.saveAndFlush(publishedProject);

        Project draftProject = new Project("draft-app", "Draft App", "Draft project", ProjectStatus.DRAFT);
        draftProject.addSkill(java);
        draftProject.addSkill(spring);
        projectRepository.saveAndFlush(draftProject);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void listContainsOnlyPublishedProjects() throws Exception {
        mockMvc.perform(get("/api/v1/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements")
                        .value(1))
                .andExpect(jsonPath("$.items[0].slug")
                        .value("published-app"));
    }

    @Test
    void detailsOfPublishedProjectIncludesSkills() throws Exception {
        mockMvc.perform(get("/api/v1/projects/published-app"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.skills",
                        hasSize(2)))
                .andExpect(jsonPath("$.skills[*].name", containsInAnyOrder(
                        "Java",
                        "Spring Boot"
                )));
    }

    @Test
    void detailsOfDraftProjectReturns404() throws Exception {
        mockMvc.perform(get("/api/v1/projects/draft-app"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Project Not Found"));
    }
}
