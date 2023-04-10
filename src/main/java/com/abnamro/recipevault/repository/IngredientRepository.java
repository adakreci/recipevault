package com.abnamro.recipevault.repository;

import com.abnamro.recipevault.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    boolean existsByNameIgnoreCase(final String name);
}