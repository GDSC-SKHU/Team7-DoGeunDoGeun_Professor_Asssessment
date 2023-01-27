package com.dugeun.dugeunbackend.domain.professor.ability;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AbilityService {

    private final AbilityRepository abilityRepository;

    @Transactional
    public Ability save(Ability ability){
        return abilityRepository.save(ability);
    }

}
