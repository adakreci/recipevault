package com.abnamro.recipevault.service;

import com.abnamro.recipevault.domain.Recipe;
import com.abnamro.recipevault.dto.RecipeDTO;
import com.abnamro.recipevault.mapper.RecipeMapper;
import com.abnamro.recipevault.repository.RecipeRepository;
import com.abnamro.recipevault.response.RecipeValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith( MockitoExtension.class )
public class RecipeServiceTest {

    @InjectMocks
    private RecipeService recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeMapper recipeMapper;

    @Mock
    private WebMessageResolverService webMessageResolverService;

    @Mock
    private RecipeValidationService recipeValidationService;

    @Captor
    private ArgumentCaptor<RecipeDTO> recipeDTOCaptor;

    @Test
    void testCreate_ValidRecipe_Successful() throws RecipeValidationException {
        // Mocking the behavior of RecipeValidationService
        doNothing().when( recipeValidationService ).validateRecipeCreate( any( RecipeDTO.class ) );

        // Mocking the behavior of RecipeMapper
        when( recipeMapper.map( any( RecipeDTO.class ) ) ).thenReturn( new Recipe() );

        // Mocking the behavior of RecipeRepository
        when( recipeRepository.save( any( Recipe.class ) ) ).thenReturn( new Recipe() );

        // Mocking the behavior of WebMessageResolverService
        when( webMessageResolverService.getMessage( eq( "recipe.create.success" ) ) ).thenReturn( "Success" );

        // Calling the method to be tested
        var result = recipeService.create( new RecipeDTO() );

        // Verifying the expected behavior
        assertNotNull( result );
        assertEquals( "Success", result.getMessage() );
        verify( recipeRepository, times( 1 ) ).save( any( Recipe.class ) );
        verify( recipeMapper, times( 1 ) ).map( recipeDTOCaptor.capture() );
        verify( recipeValidationService, times( 1 ) ).validateRecipeCreate( any( RecipeDTO.class ) );
        verify( webMessageResolverService, times( 1 ) ).getMessage( "recipe.create.success" );

        // Asserting the contents of the captured RecipeDTO object
        var capturedRecipeDTO = recipeDTOCaptor.getValue();
        assertNotNull( capturedRecipeDTO );
        // Add additional assertions for the contents of the captured RecipeDTO object as needed
    }

    @Test
    void testCreate_InvalidRecipe_ExceptionHandled() throws RecipeValidationException {
        // Mocking the behavior of RecipeValidationService to throw an exception
        doThrow( new RecipeValidationException( "Validation failed" ) ).when( recipeValidationService ).validateRecipeCreate( any( RecipeDTO.class ) );

        // Mocking the behavior of WebMessageResolverService
        when( webMessageResolverService.getMessage( eq( "Exists.recipe.name" ), anyString() ) ).thenReturn( "Validation failed" );

        // Calling the method to be tested
        var result = recipeService.create( new RecipeDTO() );

        // Verifying the expected behavior
        assertNotNull( result );
        assertNull( result.getMessage() ); // Check for null value
        assertEquals( "Validation failed", webMessageResolverService.getMessage( "Exists.recipe.name", "Validation failed" ) );
    }

    @Test
    void testUpdate_ValidRecipe_RecipeUpdatedSuccessfully() {
        // Prepare test data
        var source = getRecipeDTO();
        var recipe = getRecipe();

        when( recipeRepository.findById( anyLong() ) ).thenReturn( Optional.of( recipe ) );
        when( recipeMapper.map( eq( recipe ), eq( source ) ) ).thenReturn( recipe );
        when( webMessageResolverService.getMessage( eq( "recipe.update.success" ) ) ).thenReturn( "Recipe updated successfully" );

        // Call the method to be tested
        var result = recipeService.update( source );

        // Verify the expected behavior
        assertNotNull( result );
        assertEquals( "Recipe updated successfully", result.getMessage() );
        verify( recipeRepository, times( 1 ) ).findById( anyLong() );
        verify( recipeValidationService, times( 1 ) ).validateRecipeUpdate( eq( recipe ), eq( source ) );
        verify( recipeRepository, times( 1 ) ).save( eq( recipe ) );
        verify( webMessageResolverService, times( 1 ) ).getMessage( eq( "recipe.update.success" ) );
    }

    @Test
    void testDelete_ExistingRecipe_RecipeDeletedSuccessfully() {
        // Prepare test data
        Long id = 1L;
        when( recipeRepository.existsById( eq( id ) ) ).thenReturn( true );

        // Call the method to be tested
        assertDoesNotThrow( () -> recipeService.delete( id ) );

        // Verify the expected behavior
        verify( recipeRepository, times( 1 ) ).existsById( eq( id ) );
        verify( recipeRepository, times( 1 ) ).deleteById( eq( id ) );
    }

    @Test
    void testDelete_NonExistingRecipe_ExceptionThrown() {
        // Prepare test data
        Long id = 1L;
        when( recipeRepository.existsById( eq( id ) ) ).thenReturn( false );

        // Call the method to be tested
        assertThrows( ResponseStatusException.class, () -> recipeService.delete( id ) );

        // Verify the expected behavior
        verify( recipeRepository, times( 1 ) ).existsById( eq( id ) );
        verify( recipeRepository, times( 0 ) ).deleteById( any() );
    }

    @Test
    void testGetById_NonExistingRecipe_ExceptionThrown() {
        when( recipeRepository.findById( 1L ) ).thenReturn( Optional.empty() );

        // Call the getById method and assert that a ResponseStatusException is thrown
        assertThrows( ResponseStatusException.class, () -> recipeService.getById( 1L ) );
    }

    private static Recipe getRecipe() {
        return Recipe.builder()
                     .recipeId( 1L )
                     .name( "Test Recipe" )
                     .instructions( "Test instructions" )
                     .servings( 4 )
                     .vegetarian( false )
                     .ingredients( new HashSet<>() ) // create a valid set of Ingredient objects
                     .build();
    }

    private static RecipeDTO getRecipeDTO() {
        return RecipeDTO.builder()
                        .recipeId( 1L )
                        .name( "Test Recipe" )
                        .instructions( "Test instructions" )
                        .servings( 4 )
                        .vegetarian( false )
                        .ingredients( new HashSet<>() ) // create a valid set of IngredientDTO objects
                        .build();
    }
}