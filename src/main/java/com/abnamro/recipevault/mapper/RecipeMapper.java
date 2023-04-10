package com.abnamro.recipevault.mapper;

import com.abnamro.recipevault.domain.Recipe;
import com.abnamro.recipevault.domain.RecipeIngredient;
import com.abnamro.recipevault.dto.IngredientDTO;
import com.abnamro.recipevault.dto.RecipeDTO;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RecipeMapper {

    private final IRecipeMapper recipeMapper;
    private final IIngredientMapper ingredientMapper;

    public Recipe map( final Recipe recipe,
                       final RecipeDTO requestRecipe) {
        if (requestRecipe == null) {
            return recipe;
        }

        recipeMapper.map(requestRecipe, recipe);

        if (CollectionUtils.isEmpty(recipe.getIngredients())) {
            recipe.setIngredients(ingredientMapper.mapToDomains(requestRecipe.getIngredients()));
        } else {
            recipe.setIngredients(map(recipe.getIngredients(), requestRecipe.getIngredients()));
        }
        return recipe;
    }

    public Set<RecipeIngredient> map( final Set<RecipeIngredient> ingredients,
                                      final Set<IngredientDTO> ingredientsRequest) {
        if (CollectionUtils.isEmpty(ingredientsRequest)) {
            return Collections.emptySet();
        }
        final Map<String, RecipeIngredient> mapByName = ingredients.stream()
                                                                   .collect(Collectors.toMap(recipeIngredient -> recipeIngredient.getIngredient().getName(), Function.identity()));
        return ingredientsRequest.stream().map(ingredientRequest -> {
            final RecipeIngredient foundRecipe = mapByName.get(ingredientRequest.getName());
            if (foundRecipe != null) {
                return ingredientMapper.map(ingredientRequest, foundRecipe);
            } else {
                return ingredientMapper.mapToDomain(ingredientRequest);
            }
        }).collect(Collectors.toSet());
    }

    public RecipeDTO map(final Recipe recipe) {
        return recipeMapper.map(recipe);
    }

    public List<RecipeDTO> map(final List<Recipe> recipe) {
        return recipeMapper.map(recipe);
    }

    public Recipe map(final RecipeDTO recipe) {
        return recipeMapper.map(recipe);
    }
}