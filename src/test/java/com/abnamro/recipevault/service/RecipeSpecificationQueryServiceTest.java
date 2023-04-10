package com.abnamro.recipevault.service;

import com.abnamro.recipevault.domain.Recipe;
import com.abnamro.recipevault.request.RecipeRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ExtendWith( MockitoExtension.class )
public class RecipeSpecificationQueryServiceTest {

    @InjectMocks
    private RecipeSpecificationQueryService recipeSpecificationQueryService;

    @Mock
    private RecipeRequest recipeRequest;

    @Mock
    private Root<Recipe> root;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Recipe> criteriaQuery;

    @Mock
    private Predicate predicate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks( this );
    }

    @Test
    void testHandleSameFields() {
        // Arrange
        when( recipeRequest.getVegetarian() ).thenReturn( true );
        when( criteriaBuilder.equal( any(), anyBoolean() ) ).thenReturn( predicate );

        // Act
        recipeSpecificationQueryService.handleSameFields( recipeRequest, root, criteriaBuilder, new ArrayList<>() );

        // Assert
        verify( criteriaBuilder, times( 1 ) ).equal( any(), anyBoolean() );
    }
}
