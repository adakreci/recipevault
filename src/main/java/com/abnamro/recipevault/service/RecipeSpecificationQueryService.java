package com.abnamro.recipevault.service;

import com.abnamro.recipevault.domain.Recipe;
import com.abnamro.recipevault.domain.RecipeIngredient;
import com.abnamro.recipevault.request.RecipeRequest;
import jakarta.persistence.criteria.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeSpecificationQueryService {

    public Specification<Recipe> getSearchQuery( final RecipeRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            handleSameFields(request, root, criteriaBuilder, predicates);
            if (CollectionUtils.isNotEmpty(request.getExcludeIngredients())
                    || CollectionUtils.isNotEmpty(request.getIncludeIngredients())) {
                Join<Recipe, RecipeIngredient> join = root.join("ingredients");
                Path<String> ingredientName = join.get("ingredient").get("name");

                if (CollectionUtils.isNotEmpty(request.getExcludeIngredients())) {
                    predicates.add(ingredientName.in(request.getExcludeIngredients()).not());
                }
                if (CollectionUtils.isNotEmpty(request.getIncludeIngredients())) {
                    predicates.add(ingredientName.in(request.getIncludeIngredients()));
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private void handleSameFields(final RecipeRequest request,
                                  Root<Recipe> root,
                                  CriteriaBuilder criteriaBuilder,
                                  List<Predicate> predicates) {
        if (Boolean.TRUE.equals(request.getVegetarian())) {
            predicates.add(criteriaBuilder.isTrue(root.get("vegetarian")));
        }
        if (Boolean.FALSE.equals(request.getVegetarian())) {
            predicates.add(criteriaBuilder.isFalse(root.get("vegetarian")));
        }
        if (request.getServings() != null) {
            predicates.add(criteriaBuilder.equal(root.get("servings"), request.getServings()));
        }
        if (StringUtils.isNotBlank(request.getSearchKey())) {
            predicates.add(criteriaBuilder.like(root.get("instructions"), "%" + request.getSearchKey() + "%"));
        }
    }
}