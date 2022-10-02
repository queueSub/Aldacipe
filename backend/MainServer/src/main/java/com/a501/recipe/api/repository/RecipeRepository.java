package com.a501.recipe.api.repository;

import com.a501.recipe.api.domain.entity.Food;
import com.a501.recipe.api.domain.entity.Nutrient;
import com.a501.recipe.api.domain.entity.Recipe;
import com.a501.recipe.api.dto.ingredient.IngredientDto;
import com.a501.recipe.api.dto.nutrient.NutrientDto;
import com.a501.recipe.api.dto.nutrient.RecipeNutrientDto;
import com.a501.recipe.api.dto.recipe.RecipeAndFoodSearchResponseDto;
import com.a501.recipe.api.dto.recipe.RecipeDetailPageResponseDto;
import com.a501.recipe.api.dto.recipe.RecipeDto;
import com.a501.recipe.api.dto.recipe.RecipeThumbNailResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe,Long> {

    @Query("select r from Recipe r where id in (:ids)")
    List<Recipe> searchRecipeByIdList(@Param("ids") List<Long> idList);


    // name query
    @Query("select new com.a501.recipe.api.dto.recipe.RecipeDto(r)" +
            " from Recipe r where r.name like :keyword%")
    Optional<List<RecipeDto>> searchRecipeByNameLike(@Param("keyword") String keyword);
    Optional<List<Recipe>> findRecipeByNameLike(String keyword);
    @Query("select distinct new com.a501.recipe.api.dto.recipe.RecipeDto(r)" +
            " from Recipe r join r.recipeIngredients ri where r.name like :keyword% and ri.id in (:ingredientIdList)")
    Optional<List<RecipeDto>> searchRecipeByNameLikeAndIngredients(@Param("keyword") String keyword, @Param("ingredientIdList") List<Long> ingredientIdList);
    @Query("select distinct r from Recipe r join fetch r.recipeIngredients ri join fetch ri.ingredient i where r.name like :keyword%")
    Optional<List<Recipe>> searchRecipeByNameLikeWithIngredient(String keyword);



    @Query("select distinct r" +
            " from Recipe r" +
            " join fetch r.nutrient n" +
            " join fetch r.manuals m" +
            " where r.id=(:id)")
    Optional<Recipe> searchRecipeWithNutrientAndManualById(@Param("id") Long id);

    @Query("select r from Recipe r join fetch r.nutrient n where r.id=:id")
    Optional<Recipe> searchRecipeWithNutrientById(@Param("id") Long id);
    @Query("select f from Food f join fetch f.nutrient n where f.id=:id")
    Optional<Food> searchFoodWithNutrientById(@Param("id") Long id);

    @Query("select new com.a501.recipe.api.dto.recipe.RecipeAndFoodSearchResponseDto(f) from Food f where f.name like :keyword% ")
    List<RecipeAndFoodSearchResponseDto> searchAllFoodByNameLike(@Param("keyword") String keyword);

    @Query(nativeQuery = true,
            value = "select e.recipe_id, r.name, r.image_big, sum(e.score)/count(*) as avg_score from evaluation e inner join recipe r where e.recipe_id=r.id group by e.recipe_id order by avg_score desc limit 20 ")
    List<RecipeThumbNailResponseDto> searchTop20BestRecipeFrom(@Param("fromDate") LocalDate fromDate);


}
