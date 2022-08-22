package com.example.experienceexchange.repository;

import com.example.experienceexchange.model.Direction;
import com.example.experienceexchange.repository.interfaceRepo.IDirectionRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DirectionRepository extends HibernateAbstractDao<Direction,Long> implements IDirectionRepository {
}
