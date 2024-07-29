package xyz.nillpoe.crafting.listener;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import xyz.nillpoe.crafting.CustomCrafting;

import java.util.List;

@AllArgsConstructor
public class ItemCraftListener implements Listener {

    private final CustomCrafting plugin;

    @EventHandler
    public void onRecipe(PrepareItemCraftEvent event) {
        if (!(event.getView().getPlayer() instanceof Player player)) return;

        CraftingInventory inventory = event.getInventory();
        ItemStack[] matrix = inventory.getMatrix();

        List<ShapedRecipe> recipeList = plugin.getRecipeRepository().getRecipes();
        recipeList.removeIf((recipe) -> {
            String recipeId = recipe.getKey().getKey();
            if (!player.hasPermission("nillpoe.crafting.craft.%s".formatted(recipeId))) return true;

            for (int i = 0; i < 9; i++) {
                RecipeChoice recipeChoice = recipe.getChoiceMap().get(String.valueOf(i).charAt(0));
                if (recipeChoice == null) continue;

                ItemStack matrixSlot = matrix[i];
                if (matrixSlot == null) return true;

                if (recipeChoice.test(matrixSlot)) return true;
            }

            return false;
        });

        if (!recipeList.isEmpty()) {
            inventory.setResult(recipeList.getFirst().getResult());
        }
    }
}