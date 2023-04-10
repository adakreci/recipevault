package com.abnamro.recipevault.controller;

import com.abnamro.recipevault.dto.RecipeDTO;
import com.abnamro.recipevault.request.RecipeRequest;
import com.abnamro.recipevault.response.RecipeResponse;
import com.abnamro.recipevault.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Tag(name = "Recipes", description = "The recipes vault API")
@RestController
@RequestMapping(value = "/recipes")
public class RecipeResource {

    private final RecipeService recipeService;

    @PostMapping(value = "/add")
    @ApiResponse(responseCode = "201")
    @Operation(description = "store the requested recipe and return all previously saved recipes")
    public ResponseEntity<RecipeResponse> createRecipe( @RequestBody @Valid final RecipeDTO recipeDTO) {
        return new ResponseEntity<>(recipeService.create(recipeDTO), HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    @ApiResponse(responseCode = "200")
    @Operation(description = "update the requested recipe and return all previously saved recipes")
    public ResponseEntity<RecipeResponse> updateRecipe(@RequestBody @Valid final RecipeDTO recipeDTO) {
        return ResponseEntity.ok(recipeService.update(recipeDTO));
    }

    @GetMapping("/{id}")
    @Operation(description = "get a specific recipe details by id")
    public ResponseEntity<RecipeDTO> getRecipe(@PathVariable final Long id) {
        return ResponseEntity.ok(recipeService.getById(id));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(description = "delete specific recipe")
    public ResponseEntity<Void> deleteRecipe(@PathVariable final Long id) {
        recipeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(description = "retrieve all recipes based on the mentioned criteria")
    public ResponseEntity<List<RecipeDTO>> search(
            @RequestParam(name = "vegetarian", required = false) @Parameter(description = "vegetarian recipe only") Boolean vegetarian,
            @RequestParam(name = "servings", required = false) @Parameter(description = "recipe with specific servings") Integer servings,
            @RequestParam(name = "includeIngredients", required = false) @Parameter(description = "recipes that include certain ingredients") List<String> includeIngredients,
            @RequestParam(name = "excludeIngredients", required = false) @Parameter(description = "recipes that exclude certain ingredients") List<String> excludeIngredients,
            @RequestParam(name = "searchKey", required = false) @Parameter(description = "recipes that has the search key within the instructions") String searchKey) {
        return ResponseEntity.ok(recipeService.findAll( RecipeRequest.builder()
                                                                     .vegetarian(vegetarian)
                                                                     .servings(servings)
                                                                     .searchKey(searchKey)
                                                                     .excludeIngredients(excludeIngredients)
                                                                     .includeIngredients(includeIngredients)
                                                                     .build()));
    }

}