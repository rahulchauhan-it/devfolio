package com.rahulchauhan.devfolio.skill;

public record SkillResponse(
        String name,
        SkillCategory category,
        Short proficiency,
        String icon
) {
}
