package com.rahulchauhan.devfolio.project;

import com.rahulchauhan.devfolio.common.PageResponse;
import com.rahulchauhan.devfolio.skill.Skill;
import com.rahulchauhan.devfolio.skill.SkillResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    private static final Comparator<Skill> SKILL_ORDER = Comparator.comparing(
                    Skill::getProficiency,
                    Comparator.reverseOrder()
            )
            .thenComparing(Skill::getName,
                    String.CASE_INSENSITIVE_ORDER);


    public PageResponse<ProjectSummaryResponse> list(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by(Sort.Order.desc("featured"), Sort.Order.asc("displayOrder")));

        Page<ProjectSummaryResponse> result = projectRepository.findByStatus(ProjectStatus.PUBLISHED, pageable).map(this::toSummaryResponse);
        return PageResponse.from(result);
    }

    private ProjectSummaryResponse toSummaryResponse(Project project) {
        List<String> skillNames = project.getSkills().stream().sorted(SKILL_ORDER).map(Skill::getName).toList();

        return new ProjectSummaryResponse(project.getSlug(), project.getTitle(), project.getSummary(), project.getCoverImageUrl(), project.getFeatured(), project.getStartedOn(), skillNames);
    }

    private ProjectDetailResponse toDetailResponse(Project project) {
        List<SkillResponse> skills = project.getSkills().stream().sorted(SKILL_ORDER).map(skill -> new SkillResponse(skill.getName(), skill.getCategory(), skill.getProficiency(), skill.getIcon())).toList();

        return new ProjectDetailResponse(project.getSlug(), project.getTitle(), project.getSummary(), project.getCoverImageUrl(), project.getFeatured(), project.getStartedOn(), project.getCaseStudy(), project.getRepoUrl(), project.getLiveUrl(), skills);
    }

    public ProjectDetailResponse getBySlug(String slug) {
        Project project = projectRepository.findBySlugAndStatus(slug, ProjectStatus.PUBLISHED).orElseThrow(() -> new ProjectNotFoundException(slug));
        return toDetailResponse(project);
    }
}
