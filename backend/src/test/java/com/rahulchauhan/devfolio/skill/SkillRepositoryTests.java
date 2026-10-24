package com.rahulchauhan.devfolio.skill;

import com.rahulchauhan.devfolio.TestcontainersConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import(TestcontainersConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SkillRepositoryTests {

    @Autowired
    SkillRepository skillRepository;
    @Autowired
    EntityManager entityManager;

    @Test
    void rejectsDuplicateName() {
        Skill skill1 = new Skill("Spring Boot", SkillCategory.BACKEND);
        Skill skill2 = new Skill("Spring Boot", SkillCategory.LANGUAGE);

        skillRepository.saveAndFlush(skill1);

        DataIntegrityViolationException ex = assertThrows(DataIntegrityViolationException.class, () -> skillRepository.saveAndFlush(skill2));

        assertThat(ex.getMessage()).containsIgnoringCase("uq_skill_name");
    }

    @ParameterizedTest
    @ValueSource(shorts = {1, 5})
    void acceptsValidProficiency(short proficiency) {
        Skill skill = new Skill("Java" + proficiency, SkillCategory.LANGUAGE);
        skill.setProficiency(proficiency);

        Skill saved = skillRepository.saveAndFlush(skill);

        assertThat(saved.getId()).isNotNull();

        entityManager.clear();

        Skill reloaded = skillRepository.findById(saved.getId()).orElseThrow();
        assertThat(reloaded.getProficiency()).isEqualTo(proficiency);
    }

    @ParameterizedTest
    @ValueSource(shorts = {0, 6})
    void rejectsInvalidProficiency(short proficiency) {
        Skill skill = new Skill("Java" + proficiency, SkillCategory.LANGUAGE);
        skill.setProficiency(proficiency);

        DataIntegrityViolationException ex = assertThrows(DataIntegrityViolationException.class, () -> skillRepository.saveAndFlush(skill));
        assertThat(ex.getMessage()).containsIgnoringCase("ck_skill_proficiency");
    }
}
