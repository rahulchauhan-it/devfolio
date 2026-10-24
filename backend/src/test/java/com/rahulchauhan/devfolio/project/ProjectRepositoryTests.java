package com.rahulchauhan.devfolio.project;

import com.rahulchauhan.devfolio.TestcontainersConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProjectRepositoryTests {

    @Autowired
    ProjectRepository projectRepository;

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
}
