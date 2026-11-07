package com.rahulchauhan.devfolio.project;

import com.rahulchauhan.devfolio.common.PageResponse;
import com.rahulchauhan.devfolio.skill.SkillCategory;
import com.rahulchauhan.devfolio.skill.SkillResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ProjectService projectService;

    @Test
    void listReturnsProjects() throws Exception {

        ProjectSummaryResponse project = new ProjectSummaryResponse(
                "jobportal",
                "Job Portal",
                "A job portal web app",
                "cover.jpg",
                true,
                LocalDate.of(2026, 1, 1),
                List.of("Java", "Spring Boot")
        );

        PageResponse<ProjectSummaryResponse> response = new PageResponse<>(
                List.of(project),
                0,
                10,
                1,
                1
        );

        when(projectService.list(0, 10))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].slug")
                        .value("jobportal"))
                .andExpect(jsonPath("$.items[0].startedOn")
                        .value("2026-01-01"))
                .andExpect(jsonPath("$.page")
                        .value(0))
                .andExpect(jsonPath("$.totalElements")
                        .value(1));
    }

    @Test
    void getMissingProjectReturns404() throws Exception {

        when(projectService.getBySlug("nope"))
                .thenThrow(new ProjectNotFoundException("nope"));

        mockMvc.perform(get("/api/v1/projects/nope"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title")
                        .value("Project Not Found"))
                .andExpect(jsonPath("$.detail")
                        .value("Project not found: nope"));
    }

    @Test
    void getProjectBySlugReturnsProject() throws Exception {

        ProjectDetailResponse response = new ProjectDetailResponse(
                "jobportal",
                "Job Portal",
                "A job portal web app",
                "cover.jpg",
                true,
                LocalDate.of(2026, 1, 1),
                "Case Study",
                "https://github.com/demo",
                "https://demo.com",
                List.of(
                        new SkillResponse(
                                "Java",
                                SkillCategory.LANGUAGE,
                                (short) 5,
                                "java.svg"
                        )
                )
        );

        when(projectService.getBySlug("jobportal"))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/projects/jobportal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug")
                        .value("jobportal"))
                .andExpect(jsonPath("$.skills[0].name")
                        .value("Java"));
    }

    @Test
    void listBindsPageAndSizeParameters() throws Exception {
        when(projectService.list(2, 5))
                .thenReturn(new PageResponse<>(
                        List.of(),
                        2,
                        5,
                        0,
                        0
                ));
        mockMvc.perform(
                        get("/api/v1/projects")
                                .param("page", "2")
                                .param("size", "5")
                )
                .andExpect(status().isOk());
        verify(projectService).list(2, 5);
    }
}
