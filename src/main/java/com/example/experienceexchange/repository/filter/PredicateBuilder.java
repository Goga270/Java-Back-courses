package com.example.experienceexchange.repository.filter;

import java.util.List;

public interface PredicateBuilder {

    String getGroup(List<SearchCriteria> searchCriteriaGroupingByFieldName, String fieldName);
}
