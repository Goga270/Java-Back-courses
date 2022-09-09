package com.example.experienceexchange.repository.filter;

import java.util.List;
import java.util.Map;

public interface IFilterProvider {

    String createFilterQuery(Map<String, List<SearchCriteria>> searchMap);

    Map<String, List<SearchCriteria>> createSearchMap(List<SearchCriteria> filters);
}
