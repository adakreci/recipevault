package com.abnamro.recipevault.controller;

import com.abnamro.recipevault.dto.IngredientDTO;
import com.abnamro.recipevault.dto.RecipeDTO;
import com.abnamro.recipevault.response.RecipeResponse;
import com.abnamro.recipevault.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class )
class RecipeResourceTest {

    @InjectMocks
    private RecipeResource recipeResource;

    @Mock
    private RecipeService recipeService;

    @Test
    void testCreateRecipe_Success() {
        // Create a sample RecipeDTO object for request body
        var recipeDTO = createRecipeDTO();
        // Mock the recipeService.create() method
        var recipeResponse = RecipeResponse.builder()
                                           .message( "Recipe created successfully" )
                                           .recipes( Arrays.asList( recipeDTO ) )
                                           .build();
        when( recipeService.create( recipeDTO ) ).thenReturn( recipeResponse );

        // Call the createRecipe method and assert the response
        var response = recipeResource.createRecipe( recipeDTO );
        assertEquals( HttpStatus.CREATED, response.getStatusCode() );
        assertNotNull( response.getBody() );
        assertEquals( recipeResponse.getMessage(), response.getBody().getMessage() );
        assertEquals( recipeResponse.getRecipes(), response.getBody().getRecipes() );
    }

    @Test
    void testUpdateRecipe_Success() {
        // Create a sample RecipeDTO object for request body
        var recipeDTO = createRecipeDTO();

        // Mock the recipeService.update() method
        var recipeResponse = RecipeResponse.builder()
                                           .message( "Recipe updated successfully" )
                                           .recipes( Arrays.asList( recipeDTO ) )
                                           .build();
        when( recipeService.update( recipeDTO ) ).thenReturn( recipeResponse );

        // Call the updateRecipe method and assert the response
        var response = recipeResource.updateRecipe( recipeDTO );
        assertEquals( HttpStatus.OK, response.getStatusCode() );
        assertNotNull( response.getBody() );
        assertEquals( recipeResponse.getMessage(), response.getBody().getMessage() );
        assertEquals( recipeResponse.getRecipes(), response.getBody().getRecipes() );
    }

    @Test
    void testGetRecipe_Success() {
        // Create a sample RecipeDTO object for the expected response
        var recipeDTO = createRecipeDTO();

        // Mock the recipeService.getById() method
        when( recipeService.getById( 1L ) ).thenReturn( recipeDTO );

        // Call the getRecipe method and assert the response
        var response = recipeResource.getRecipe( 1L );
        assertEquals( HttpStatus.OK, response.getStatusCode() );
        assertNotNull( response.getBody() );
        assertEquals( recipeDTO, response.getBody() );
    }

    @Test
    void testDeleteRecipe_Success() {
        // Mock the recipeService.delete() method
        doNothing().when( recipeService ).delete( 1L );

        // Call the deleteRecipe method and assert the response
        var response = recipeResource.deleteRecipe( 1L );
        assertEquals( HttpStatus.NO_CONTENT, response.getStatusCode() );
        assertNull( response.getBody() );
    }

    // Helper method to create a sample RecipeDTO object for testing
    private RecipeDTO createRecipeDTO() {
        var recipeDTO = RecipeDTO.builder()
                                 .recipeId( 1L )
                                 .name( "Recipe Name" )
                                 .instructions( "Recipe Instructions" )
                                 .servings( 4 )
                                 .vegetarian( true )
                                 .ingredients( new HashSet<>() )
                                 .build();
        var ingredient1 = IngredientDTO.builder()
                                       .ingredientId( 1L )
                                       .name( "Ingredient 1" )
                                       .build();
        var ingredient2 = IngredientDTO.builder()
                                       .ingredientId( 2L )
                                       .name( "Ingredient 2" )
                                       .build();
        recipeDTO.getIngredients().add( ingredient1 );
        recipeDTO.getIngredients().add( ingredient2 );

        return recipeDTO;
    }
}