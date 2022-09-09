package com.example.experienceexchange.repository.filter;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class JpqlFilterProvider implements IFilterProvider {

    private final String FORMAT_AND = "AND ";

    private final List<String> parametersForFilter;
    private final IPredicateBuilder predicateBuilder;

    public JpqlFilterProvider(List<String> parametersForFilter,
                              IPredicateBuilder predicateBuilder) {
        this.parametersForFilter = parametersForFilter;
        this.predicateBuilder = predicateBuilder;
    }

    @Override
    public String createFilterQuery(Map<String, List<SearchCriteria>> searchMap) {
        if (searchMap == null) {
            return "";
        }

        StringBuilder filterQuery = new StringBuilder("");
        searchMap.forEach((key, value) -> {
                    if (!value.isEmpty()) {
                        filterQuery.append(FORMAT_AND).append(predicateBuilder.getGroup(value, key));
                    }
                }
        );
        return filterQuery.toString();

    }

    @Override
    public Map<String, List<SearchCriteria>> createSearchMap(List<SearchCriteria> filters) {
        if (filters.isEmpty()) {
            return null;
        }
        Map<String, List<SearchCriteria>> maps = new LinkedHashMap<>();
        parametersForFilter
                .forEach(field -> maps.put(field, filters
                        .stream()
                        .filter(sc -> field.contains(sc.getKey()))
                        .collect(Collectors.toList()))
                );
        return maps;
    }
}
