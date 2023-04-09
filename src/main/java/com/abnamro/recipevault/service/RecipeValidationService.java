package com.abnamro.recipevault.service;

import com.abnamro.recipevault.domain.Recipe;
import com.abnamro.recipevault.domain.RecipeIngredient;
import com.abnamro.recipevault.dto.IngredientDTO;
import com.abnamro.recipevault.dto.RecipeDTO;
import com.abnamro.recipevault.repository.IngredientRepository;
import com.abnamro.recipevault.repository.RecipeRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RecipeValidationService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final WebMessageResolverService webMessageResolverService;

    public String validateRecipeCreate(final RecipeDTO recipe) {
        final boolean recipeExists = recipeRepository.existsByNameIgnoreCase(recipe.getName());
        if (recipeExists) {
            return webMessageResolverService.getMessage("Exists.recipe.name", recipe.getName());
        }

        for (final IngredientDTO ingredient : recipe.getIngredients()) {
            final boolean ingredientExists = ingredientRepository.existsByNameIgnoreCase(ingredient.getName());
            if (ingredientExists) {
                return webMessageResolverService.getMessage("Exists.ingredient.name", ingredient.getName());
            }
        }
        return null;
    }

    public String validateRecipeUpdate(final Recipe recipe,
                                       final RecipeDTO recipeRequest) {
        if (ObjectUtils.notEqual(recipe.getName(), recipeRequest.getName())) {
            final boolean recipeExists = recipeRepository.existsByNameIgnoreCase(recipeRequest.getName());
            if (recipeExists) {
                return webMessageResolverService.getMessage("Exists.recipe.name", recipeRequest.getName());
            }
        }

        final Map<Long, RecipeIngredient> mapByIngredientId = recipe.getIngredients().stream()
                                                                    .collect(Collectors.toMap(recipeIngredient -> recipeIngredient.getIngredient().getIid(), Function.identity()));
        for (final IngredientDTO ingredient : recipeRequest.getIngredients()) {
            final RecipeIngredient foundMatch = mapByIngredientId.get(ingredient.getIid());
            if (foundMatch == null
                    || (foundMatch.getIngredient() != null
                    && ObjectUtils.notEqual(ingredient.getName(), foundMatch.getIngredient().getName()))) {
                final boolean ingredientExists = ingredientRepository.existsByNameIgnoreCase(ingredient.getName());
                if (ingredientExists) {
                    return webMessageResolverService.getMessage("Exists.ingredient.name", ingredient.getName());
                }
            }
        }
        return null;
    }

}