package com.abnamro.recipevault.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "model view for recipe")
public class RecipeDTO {

    @ApiModelProperty(value = "id for recipe")
    private Long rid;

    @ApiModelProperty(value = "name of the recipe")
    private String name;

    @ApiModelProperty(value = "instructions for a yummy recipe")
    private String instructions;

    @ApiModelProperty(value = "how many servings is the recipe for")
    private Integer servings;

    @ApiModelProperty(value = "vegetarian recipe")
    private Boolean vegetarian;

    @ApiModelProperty(value = "ingredients necessary to prepare the recipe")
    private Set<IngredientDTO> ingredients;
}