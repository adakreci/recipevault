package com.abnamro.recipevault.mapper;

import com.abnamro.recipevault.domain.IngredientUnit;
import com.abnamro.recipevault.domain.MeasureUnit;
import org.mapstruct.EnumMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper
public interface UnitMapper {

    @EnumMapping(nameTransformationStrategy = "suffix", configuration = "_UNIT")
    MeasureUnit map( final IngredientUnit unit);

    @InheritInverseConfiguration
    IngredientUnit map(MeasureUnit unit);
}