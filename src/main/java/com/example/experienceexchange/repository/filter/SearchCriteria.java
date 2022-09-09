package com.example.experienceexchange.repository.filter;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
// TODO: ВАЛИДАЦИЮ В МАССИВЕ ПРИДУМАТЬ
@Getter
@Setter
public class SearchCriteria {

    @NotNull
    private String key;

    @NotNull
    private String value;
}
