package com.abnamro.recipevault.service;

import com.abnamro.recipevault.dto.IngredientDTO;
import com.abnamro.recipevault.dto.RecipeDTO;
import com.abnamro.recipevault.repository.IngredientRepository;
import com.abnamro.recipevault.repository.RecipeRepository;
import com.abnamro.recipevault.response.RecipeValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith( MockitoExtension.class )
public class RecipeValidationServiceTest {

    @InjectMocks
    private RecipeValidationService recipeValidationService;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private WebMessageResolverService webMessageResolverService;

    @BeforeEach
    void setUp() {
        Mockito.reset( recipeRepository, ingredientRepository, webMessageResolverService );
    }

    @Test
    void testValidateRecipeCreate_RecipeExists_ThrowsException() throws RecipeValidationException {
        // Arrange
        var recipeRequest = RecipeDTO.builder()
                                     .name( "Recipe1" )
                                     .ingredients( new HashSet<>( Arrays.asList(
                                             IngredientDTO.builder().name( "Ingredient1" ).build(),
                                             IngredientDTO.builder().name( "Ingredient2" ).build()
                                     ) ) )
                                     .build();
        when( recipeRepository.existsByNameIgnoreCase( anyString() ) ).thenReturn( true );

        // Act & Assert
        assertThrows( RecipeValidationException.class, () -> {
            recipeValidationService.validateRecipeCreate( recipeRequest );
        } );

        // Verify
        verify( recipeRepository, times( 1 ) ).existsByNameIgnoreCase( eq( "Recipe1" ) );
        verify( ingredientRepository, never() ).existsByNameIgnoreCase( anyString() );
        verify( webMessageResolverService, times( 1 ) ).getMessage( eq( "Exists.recipe.name" ), eq( "Recipe1" ) );
    }

    @Test
    void testValidateRecipeCreate_IngredientExists_ThrowsException() {
        // Create a RecipeDTO with an existing ingredient
        var ingredientDTO = IngredientDTO.builder()
                                         .name( "ingredient1" )
                                         .build();
        var recipeDTO = RecipeDTO.builder()
                                 .name( "recipe1" )
                                 .ingredients( new HashSet<>( Arrays.asList( ingredientDTO ) ) )
                                 .build();

        // Stub the ingredientRepository.existsByNameIgnoreCase() method to return true
        given( ingredientRepository.existsByNameIgnoreCase( "ingredient1" ) ).willReturn( true );

        // Invoke the method under test and assert the expected exception
        assertThrows( RecipeValidationException.class, () -> {
            recipeValidationService.validateRecipeCreate( recipeDTO );
        } );

        // Verify that the ingredientRepository.existsByNameIgnoreCase() method was called with the correct argument
        verify( ingredientRepository ).existsByNameIgnoreCase( "ingredient1" );
    }

    @Test
    void testValidateRecipeCreate_NoDuplicates_NoExceptionsThrown() throws RecipeValidationException {
        // Arrange
        var recipeRequest = RecipeDTO.builder()
                                     .name( "Recipe1" )
                                     .ingredients( new HashSet<>( Arrays.asList(
                                             IngredientDTO.builder().name( "Ingredient1" ).build(),
                                             IngredientDTO.builder().name( "Ingredient2" ).build()
                                     ) ) )
                                     .build();
        when( recipeRepository.existsByNameIgnoreCase( anyString() ) ).thenReturn( false );
        when( ingredientRepository.existsByNameIgnoreCase( anyString() ) ).thenReturn( false );

        // Act & Assert
        assertDoesNotThrow( () -> {
            recipeValidationService.validateRecipeCreate( recipeRequest );
        } );

        // Verify
        verify( recipeRepository, times( 1 ) ).existsByNameIgnoreCase( eq( "Recipe1" ) );
        verify( ingredientRepository, times( 2 ) ).existsByNameIgnoreCase( anyString() );
        verify( webMessageResolverService, never() ).getMessage( anyString(), anyString() );
    }

}
