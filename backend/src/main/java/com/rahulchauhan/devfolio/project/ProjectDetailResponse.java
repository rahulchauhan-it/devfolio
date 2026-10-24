package com.rahulchauhan.devfolio.project;

import com.rahulchauhan.devfolio.skill.SkillResponse;

import java.time.LocalDate;
import java.util.List;

public record ProjectDetailResponse(
        String slug,
        String title,
        String summary,
        String coverImageUrl,
        boolean featured,
        LocalDate startedOn,
        String caseStudy,
        String repoUrl,
        String liveUrl,
        List<SkillResponse> skills
) {
}
