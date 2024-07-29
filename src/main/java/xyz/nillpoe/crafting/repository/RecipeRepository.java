package xyz.nillpoe.crafting.repository;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import xyz.nillpoe.crafting.CustomCrafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class RecipeRepository {

    private final CustomCrafting plugin;
    private final Map<String, ShapedRecipe> recipeMap = new HashMap<>();

    public List<ShapedRecipe> getRecipes() {
        return new ArrayList<>(recipeMap.values());
    }

    public ShapedRecipe getRecipe(String id) {
        return recipeMap.get(id);
    }

    public void setRecipe(String id, ShapedRecipe recipe) {
        recipeMap.put(id, recipe);
    }

    public void deleteRecipe(String id) {
        recipeMap.remove(id);
    }

    public void load() {
        ConfigurationSection recipeSection = plugin.getConfig().getConfigurationSection("recipe");
        if (recipeSection == null) return;

        recipeMap.clear();
        recipeSection.getKeys(false).forEach(this::load);

        plugin.getLogger().info("조합법 %d개를 불러왔습니다.".formatted(recipeMap.size()));
    }

    @SuppressWarnings("ConstantConditions")
    public void load(String id) {
        ConfigurationSection recipeData = plugin.getConfig().getConfigurationSection("recipe." + id);
        if (recipeData == null) return;

        List<String> shape = recipeData.getStringList("shape");
        if (shape == null || shape.isEmpty()) return;

        ItemStack result = recipeData.getItemStack("result");
        if (result == null || result.getType() == Material.AIR) return;

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, id), result);
        recipe.shape(shape.toArray(new String[0]));

        ConfigurationSection ingredientSection = recipeData.getConfigurationSection("ingredient");
        ingredientSection.getKeys(false).forEach((key) -> {
            ItemStack ingredientStack = ingredientSection.getItemStack(key);
            if (ingredientStack == null || ingredientStack.getType() == Material.AIR) return;

            recipe.setIngredient(key.charAt(0), new RecipeChoice.ExactChoice(ingredientStack));
        });

        recipeMap.put(id, recipe);
    }

    public void save() {
        recipeMap.keySet().forEach(this::save);

        plugin.getLogger().info("조합법 %d개를 저장했습니다.".formatted(recipeMap.size()));
    }

    public void save(String id) {
        ConfigurationSection recipeData = plugin.getConfig().createSection("recipe." + id);
        ShapedRecipe recipe = recipeMap.get(id);

        recipeData.set("shape", List.of(recipe.getShape()));
        recipeData.set("result", recipe.getResult());
        recipe.getChoiceMap().forEach((key, ingredient) -> recipeData.set("ingredient." + key, ingredient == null ? null : ingredient.getItemStack()));
    }
}