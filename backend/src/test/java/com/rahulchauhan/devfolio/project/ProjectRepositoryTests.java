package com.rahulchauhan.devfolio.project;

import com.rahulchauhan.devfolio.TestcontainersConfig;
import com.rahulchauhan.devfolio.skill.Skill;
import com.rahulchauhan.devfolio.skill.SkillCategory;
import com.rahulchauhan.devfolio.skill.SkillRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import(TestcontainersConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProjectRepositoryTests {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    void savesProjectAndFindsItBySlug() {
        Project project = new Project("jobportal", "Job Portal", "A job portal web app", ProjectStatus.DRAFT);

        projectRepository.saveAndFlush(project);
        entityManager.clear();
        Optional<Project> found = projectRepository.findBySlug("jobportal");

        assertThat(found).isPresent();

        Project savedProject = found.orElseThrow();

        assertThat(savedProject.getTitle()).isEqualTo("Job Portal");
        assertThat(savedProject.getStatus()).isEqualTo(ProjectStatus.DRAFT);

        assertThat(savedProject.getFeatured()).isFalse();
        assertThat(savedProject.getDisplayOrder()).isZero();

        assertThat(savedProject.getId()).isNotNull();
        assertThat(savedProject.getCreatedAt()).isNotNull();
        assertThat(savedProject.getUpdatedAt()).isNotNull();
        assertThat(savedProject.getVersion()).isNotNull();
    }

    @Test
    void rejectsDuplicateSlug() {

        Project project = new Project("dup", "Music App", "This is a Music Player", ProjectStatus.PUBLISHED);
        Project duplicateProject = new Project("dup", "Quiz App", "This is a Quiz App", ProjectStatus.ARCHIVED);

        projectRepository.saveAndFlush(project);

        DataIntegrityViolationException ex = assertThrows(
                DataIntegrityViolationException.class, () -> projectRepository.saveAndFlush(duplicateProject));
        assertThat(ex.getMessage()).containsIgnoringCase("uq_project_slug");
    }

    @Test
    void savesProjectWithSkills() {

        Skill springSecurity = new Skill("Spring Security", SkillCategory.BACKEND);
        Skill docker = new Skill("Docker", SkillCategory.DEVOPS);
        skillRepository.saveAndFlush(springSecurity);
        skillRepository.saveAndFlush(docker);

        Project project = new Project("jobportal", "Job Portal", "It is a Job Portal App", ProjectStatus.DRAFT);
        project.addSkill(springSecurity);
        project.addSkill(docker);
        projectRepository.saveAndFlush(project);

        entityManager.clear();

        Project reload = projectRepository.findBySlug("jobportal").orElseThrow();

        assertThat(reload.getSkills()).hasSize(2);

        assertThat(reload.getSkills().stream().map(Skill::getName)).containsExactlyInAnyOrder("Spring Security", "Docker");

    }

}
