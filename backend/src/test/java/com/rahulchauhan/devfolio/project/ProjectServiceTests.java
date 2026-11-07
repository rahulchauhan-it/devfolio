package com.rahulchauhan.devfolio.project;

import com.rahulchauhan.devfolio.common.PageResponse;
import com.rahulchauhan.devfolio.skill.Skill;
import com.rahulchauhan.devfolio.skill.SkillCategory;
import com.rahulchauhan.devfolio.skill.SkillResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTests {

    @Mock
    ProjectRepository projectRepository;

    @InjectMocks
    ProjectService projectService;

    @Test
    void throwsWhenProjectIsNotPublishedOrMissing() {
        when(projectRepository.findBySlugAndStatus("nope", ProjectStatus.PUBLISHED))
                .thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.getBySlug("nope"));
    }

    @Test
    void getBySlugReturnsProjectDetails() {
        Project project = new Project("jobportal", "Job Portal", "A job portal web app", ProjectStatus.PUBLISHED);

        project.setCoverImageUrl("cover.jpg");
        project.setRepoUrl("https://github.com/demo");
        project.setLiveUrl("https://demo.com");
        project.setCaseStudy("Case Study");
        project.setFeatured(true);

        Skill java = new Skill("Java", SkillCategory.LANGUAGE);
        java.setProficiency((short) 5);

        Skill spring = new Skill("Spring Boot", SkillCategory.BACKEND);
        spring.setProficiency((short) 4);

        project.addSkill(java);
        project.addSkill(spring);

        when(projectRepository.findBySlugAndStatus("jobportal", ProjectStatus.PUBLISHED))
                .thenReturn(Optional.of(project));

        ProjectDetailResponse response = projectService.getBySlug("jobportal");

        assertThat(response.slug()).isEqualTo("jobportal");
        assertThat(response.title()).isEqualTo("Job Portal");
        assertThat(response.summary()).isEqualTo("A job portal web app");
        assertThat(response.coverImageUrl()).isEqualTo("cover.jpg");
        assertThat(response.caseStudy()).isEqualTo("Case Study");
        assertThat(response.repoUrl()).isEqualTo("https://github.com/demo");
        assertThat(response.liveUrl()).isEqualTo("https://demo.com");
        assertThat(response.featured()).isTrue();
        assertThat(response.skills())
                .extracting(
                        SkillResponse::name,
                        SkillResponse::category,
                        SkillResponse::proficiency
                )
                .containsExactly(
                        tuple(
                                "Java",
                                SkillCategory.LANGUAGE,
                                (short) 5
                        ),
                        tuple(
                                "Spring Boot",
                                SkillCategory.BACKEND,
                                (short) 4
                        )
                );
    }

    @Test
    void skillsAreSortedByProficiencyThenName() {
        Project project = new Project("devfolio", "Devfolio", "Demo", ProjectStatus.PUBLISHED);

        Skill java = new Skill("java", SkillCategory.LANGUAGE);
        java.setProficiency((short) 5);

        Skill python = new Skill("Python", SkillCategory.LANGUAGE);
        python.setProficiency((short) 5);

        Skill docker = new Skill("Docker", SkillCategory.DEVOPS);
        docker.setProficiency((short) 3);

        project.addSkill(java);
        project.addSkill(python);
        project.addSkill(docker);

        when(projectRepository.findBySlugAndStatus("devfolio", ProjectStatus.PUBLISHED))
                .thenReturn(Optional.of(project));

        ProjectDetailResponse response = projectService.getBySlug("devfolio");

        assertThat(response.skills()).extracting(SkillResponse::name)
                .containsExactly(
                        "java",
                        "Python",
                        "Docker"
                );
    }

    @Test
    void listClampsPageAndSize() {
        Page<Project> page = new PageImpl<>(List.of());

        when(projectRepository.findByStatus(eq(ProjectStatus.PUBLISHED), any(Pageable.class))).thenReturn(page);

        projectService.list(-5, 1000);

        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(projectRepository)
                .findByStatus(
                        eq(ProjectStatus.PUBLISHED),
                        pageableArgumentCaptor.capture()
                );

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertThat(pageable.getPageNumber()).isZero();
        assertThat(pageable.getPageSize()).isEqualTo(50);
    }

    @Test
    void listMapsProjectsToSummaryResponse() {
        Skill java = new Skill("Java", SkillCategory.LANGUAGE);
        java.setProficiency((short) 5);

        Skill spring = new Skill("Spring Boot", SkillCategory.BACKEND);
        spring.setProficiency((short) 4);

        Project project = new Project(
                "jobportal",
                "Job Portal",
                "A job portal web app",
                ProjectStatus.PUBLISHED
        );

        project.addSkill(java);
        project.addSkill(spring);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Project> page = new PageImpl<>(
                List.of(project),
                pageable,
                1
        );

        when(projectRepository.findByStatus(
                eq(ProjectStatus.PUBLISHED),
                any(Pageable.class)
        )).thenReturn(page);

        PageResponse<ProjectSummaryResponse> response = projectService.list(0, 10);
        assertThat(response.items())
                .hasSize(1);

        ProjectSummaryResponse item = response.items().getFirst();

        assertThat(item.slug()).isEqualTo("jobportal");
        assertThat(item.skills()).containsExactly(
                "Java",
                "Spring Boot"
        );

        assertThat(response.page())
                .isZero();
        assertThat(response.size())
                .isEqualTo(10);
        assertThat(response.totalElements()).isEqualTo(1);

        assertThat(response.totalPages())
                .isEqualTo(1);
    }
}
