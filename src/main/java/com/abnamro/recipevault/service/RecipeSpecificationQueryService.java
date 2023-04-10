package com.abnamro.recipevault.service;

import com.abnamro.recipevault.domain.Recipe;
import com.abnamro.recipevault.request.RecipeRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class RecipeSpecificationQueryService {

    public Specification<Recipe> getSearchQuery( final RecipeRequest request ) {
        return ( root, query, criteriaBuilder ) -> {
            List<Predicate> predicates = new ArrayList<>();

            handleSameFields( request, root, criteriaBuilder, predicates );
            handleIngredients( request, root, criteriaBuilder, predicates );

            return criteriaBuilder.and( predicates.toArray( new Predicate[0] ) );
        };
    }

    private void handleSameFields( final RecipeRequest request,
                                   Root<Recipe> root,
                                   CriteriaBuilder criteriaBuilder,
                                   List<Predicate> predicates ) {
        Optional.ofNullable( request.getVegetarian() )
                .ifPresent( vegetarian -> predicates.add( criteriaBuilder.equal( root.get( "vegetarian" ), vegetarian ) ) );
        Optional.ofNullable( request.getServings() )
                .ifPresent( servings -> predicates.add( criteriaBuilder.equal( root.get( "servings" ), servings ) ) );
        Optional.ofNullable( request.getSearchKey() )
                .filter( StringUtils::isNotBlank )
                .ifPresent( searchKey -> predicates.add( criteriaBuilder.like( root.get( "instructions" ), "%" + searchKey + "%" ) ) );
    }

    private void handleIngredients( final RecipeRequest request,
                                    Root<Recipe> root,
                                    CriteriaBuilder criteriaBuilder,
                                    List<Predicate> predicates ) {
        Predicate ingredientPredicate = Optional.ofNullable( request.getExcludeIngredients() )
                                                .map( Collection::stream )
                                                .orElse( Stream.empty() )
                                                .map( excludeIngredient -> criteriaBuilder.not( root.join( "ingredients" ).get( "ingredient" ).get( "name" ).in( excludeIngredient ) ) )
                                                .reduce( criteriaBuilder::and )
                                                .orElse( null );
        if ( ingredientPredicate != null ) {
            predicates.add( ingredientPredicate );
        }

        ingredientPredicate = Optional.ofNullable( request.getIncludeIngredients() )
                                      .map( Collection::stream )
                                      .orElse( Stream.empty() )
                                      .map( includeIngredient -> root.join( "ingredients" ).get( "ingredient" ).get( "name" ).in( includeIngredient ) )
                                      .reduce( criteriaBuilder::or )
                                      .orElse( null );
        if ( ingredientPredicate != null ) {
            predicates.add( ingredientPredicate );
        }
    }
}