package com.example.experienceexchange.repository.filter;

import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class PredicateBuilderImpl implements PredicateBuilder {

    private static final String OPEN_BRACKET = "(";
    private static final String CLOSE_BRACKET = ")";
    private static final String FORMAT_PREDICATE = " %s=%s ";
    private static final String FORMAT_OR = "OR";

    @Override
    public String getGroup(List<SearchCriteria> searchCriteriaGroupingByFieldName, String fieldName) {
        StringBuilder group = new StringBuilder(OPEN_BRACKET);
        Iterator<SearchCriteria> it = searchCriteriaGroupingByFieldName.iterator();
        while (it.hasNext()) {
            SearchCriteria sc = it.next();
            group.append(String.format(FORMAT_PREDICATE, fieldName, sc.getValue()));
            if (it.hasNext()) {
                group.append(FORMAT_OR);
            }
        }
        group.append(CLOSE_BRACKET);
            return group.toString();
    }
}
