package com.example.experienceexchange.repository.filter;

import java.util.List;

public interface IPredicateBuilder {

    String getGroup(List<SearchCriteria> searchCriteriaGroupingByParameters, String param);
}
