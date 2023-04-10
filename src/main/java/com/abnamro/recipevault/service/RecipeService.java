package com.abnamro.recipevault.service;

import com.abnamro.recipevault.domain.Recipe;
import com.abnamro.recipevault.dto.RecipeDTO;
import com.abnamro.recipevault.mapper.RecipeMapper;
import com.abnamro.recipevault.repository.RecipeRepository;
import com.abnamro.recipevault.request.RecipeRequest;
import com.abnamro.recipevault.response.RecipeResponse;
import com.abnamro.recipevault.response.RecipeValidationException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final RecipeValidationService validationService;
    private final RecipeSpecificationQueryService customQuerySpecificationService;
    private final WebMessageResolverService webMessageResolverService;

    public List<RecipeDTO> findAll( final RecipeRequest searchCriteria) {
        return recipeMapper.map(recipeRepository.findAll(customQuerySpecificationService.getSearchQuery(searchCriteria)));
    }

    @Transactional
    public RecipeResponse create(final RecipeDTO source) {
        try {
            validationService.validateRecipeCreate(source);
        } catch (RecipeValidationException ex) {
            return RecipeResponse.builder()
                                 .message(ex.getMessage())
                                 .build();
        }
        recipeRepository.save(recipeMapper.map(source));
        return RecipeResponse.builder()
                             .message(webMessageResolverService.getMessage("recipe.create.success"))
                             .recipes(findAll())
                             .build();
    }

    @Transactional
    public RecipeResponse update(final RecipeDTO source) {
        final Recipe recipe = recipeRepository.findById(source.getRecipeId())
                                              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        try {
            validationService.validateRecipeUpdate(recipe, source);
        } catch ( RecipeValidationException ex) {
            return RecipeResponse.builder()
                                 .message(ex.getMessage())
                                 .build();
        }
        recipeRepository.save(recipeMapper.map(recipe, source));
        return RecipeResponse.builder()
                             .message(webMessageResolverService.getMessage("recipe.update.success"))
                             .recipes(findAll())
                             .build();
    }

    @Transactional
    public void delete(final Long id) {
        // Check if recipe with given id exists
        if (!recipeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        try {
            recipeRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete recipe with id: " + id, e);
        }
    }

    @Transactional(readOnly = true)
    public RecipeDTO getById(final Long recipeId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (!recipeOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return recipeMapper.map(recipeOptional.get());
    }

    public List<RecipeDTO> findAll() {
        return recipeMapper.map( recipeRepository.findAll() );
    }
}