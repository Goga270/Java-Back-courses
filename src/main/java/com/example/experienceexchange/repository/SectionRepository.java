package com.example.experienceexchange.repository;

import com.example.experienceexchange.model.Section;
import com.example.experienceexchange.repository.interfaceRepo.ISectionRepository;
import org.springframework.stereotype.Repository;

@Repository
public class SectionRepository extends HibernateAbstractDao<Section, Long> implements ISectionRepository {
    //
}
