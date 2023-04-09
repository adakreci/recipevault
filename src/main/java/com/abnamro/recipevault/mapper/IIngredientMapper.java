package com.abnamro.recipevault.mapper;

import com.abnamro.recipevault.domain.RecipeIngredient;
import com.abnamro.recipevault.dto.IngredientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.Set;

@Mapper(uses = {UnitMapper.class})
public interface IIngredientMapper {

    @Mapping(target = "iid", source = "ingredient.iid")
    @Mapping(target = "name", source = "ingredient.name")
    @Mapping(target = "healthBenefit", source = "ingredient.healthBenefit")
    IngredientDTO mapToDTO( final RecipeIngredient recipe);

    @Mapping(target = "ingredient.iid", source = "iid")
    @Mapping(target = "ingredient.name", source = "name")
    @Mapping(target = "ingredient.healthBenefit", source = "healthBenefit")
    RecipeIngredient mapToDomain(final IngredientDTO recipe);

    @Mapping(target = "ingredient.iid", source = "iid")
    @Mapping(target = "ingredient.name", source = "name")
    @Mapping(target = "ingredient.healthBenefit", source = "healthBenefit")
    Set<RecipeIngredient> mapToDomains(final Set<IngredientDTO> recipes);

    @Mappings({
            @Mapping(source = "ingredientRequest.name", target = "ingredient.name"),
            @Mapping(source = "ingredientRequest.healthBenefit", target = "ingredient.healthBenefit"),
            @Mapping(source = "ingredientRequest.measure", target = "measure"),
            @Mapping(source = "ingredientRequest.unit", target = "unit")
    })
    RecipeIngredient map(final IngredientDTO ingredientRequest, @MappingTarget RecipeIngredient recipeIngredient);

}