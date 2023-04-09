package com.abnamro.recipevault.dto;

import com.abnamro.recipevault.util.MeasureUnit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "model view for ingredient")
public class IngredientDTO {

    @ApiModelProperty(value = "id for ingredient recipe")
    private Long id;

    @ApiModelProperty(value = "id for ingredient")
    private Long iid;

    @ApiModelProperty(value = "name for ingredient")
    private String name;

    @ApiModelProperty(value = "health benefits of the ingredient")
    private String healthBenefit;

    @ApiModelProperty(value = "measure of the ingredient")
    private Double measure;

    @ApiModelProperty(value = "unit of ingredient's measure")
    private MeasureUnit unit;
}