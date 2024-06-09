package com.example.localguidebe.converter;

import com.example.localguidebe.dto.LanguageSkillDTO;
import com.example.localguidebe.entity.LanguageSkill;
import org.springframework.stereotype.Component;

@Component
public class LanguageSkillToLanguageSkillDtoConverter {
    public LanguageSkillDTO convert(LanguageSkill source) {
        return new LanguageSkillDTO(source.getId(),
                                    source.getLanguage().getName(),
                                    source.getLevelDetail().getLevel());
    }
}
