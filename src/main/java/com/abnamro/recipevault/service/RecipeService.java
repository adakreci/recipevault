package com.abnamro.recipevault.service;

import com.abnamro.recipevault.domain.Recipe;
import com.abnamro.recipevault.dto.RecipeDTO;
import com.abnamro.recipevault.mapper.RecipeMapper;
import com.abnamro.recipevault.repository.RecipeRepository;
import com.abnamro.recipevault.request.RecipeRequest;
import com.abnamro.recipevault.response.RecipeResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
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

    public RecipeResponse create( final RecipeDTO source) {
        final String errorMessage = validationService.validateRecipeCreate(source);
        if (StringUtils.isNotBlank(errorMessage)) {
            return RecipeResponse.builder()
                                    .message(errorMessage)
                                    .build();
        }
        recipeRepository.save(recipeMapper.map(source));

        return RecipeResponse.builder()
                                .message(webMessageResolverService.getMessage("recipe.create.success"))
                                .recipes(findAll())
                                .build();
    }

    public RecipeResponse update(final RecipeDTO source) {
        final Recipe recipe = recipeRepository.findById(source.getRid())
                                              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        final String errorMessage = validationService.validateRecipeUpdate(recipe, source);
        if (StringUtils.isNotBlank(errorMessage)) {
            return RecipeResponse.builder()
                                    .message(errorMessage)
                                    .build();
        }
        recipeRepository.save(recipeMapper.map(recipe, source));
        return RecipeResponse.builder()
                                .message(webMessageResolverService.getMessage("recipe.update.success"))
                                .recipes(findAll())
                                .build();
    }

    public void delete(final Long id) {
        recipeRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        recipeRepository.deleteById(id);
    }

    public RecipeDTO getById(final Long recipeId) {
        return recipeMapper.map(recipeRepository.findById(recipeId)
                                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    private List<RecipeDTO> findAll() {
        return recipeMapper.map(recipeRepository.findAll());
    }
}