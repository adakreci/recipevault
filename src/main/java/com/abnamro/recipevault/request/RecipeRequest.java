package com.abnamro.recipevault.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RecipeRequest {
    private final Boolean vegetarian;
    private final Integer servings;
    private final String searchKey;
    private final List<String> includeIngredients;
    private final List<String> excludeIngredients;
}