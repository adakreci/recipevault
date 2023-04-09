package com.abnamro.recipevault.response;

import com.abnamro.recipevault.dto.RecipeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@ApiModel(value = "model view with recipes and message")
public class RecipeResponse {

    @ApiModelProperty(value = "message to be shown after a processed action")
    private String message;

    @ApiModelProperty(value = "recipes retrieved")
    private List<RecipeDTO> recipes;
}