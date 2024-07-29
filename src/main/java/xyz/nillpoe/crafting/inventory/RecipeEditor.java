package xyz.nillpoe.crafting.inventory;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;

public class RecipeEditor extends BaseInventory {

    @Getter
    private final String recipeId;

    public RecipeEditor(String recipeId) {
        super("레시피 편집 [%s]".formatted(recipeId), 6, false);

        this.recipeId = recipeId;
    }

    @Override
    protected void initializeInventory(Inventory inventory, Player player) {
        for (int i = 0; i < rows * 9; i++) {
            inventory.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }

        List.of(
                10, 11, 12,
                19, 20, 21, 23,
                28, 29, 30
        ).forEach((slot) -> inventory.setItem(slot, null));

        ItemStack executeBtn = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        inventory.setItem(25, executeBtn);
    }

    @Override
    public void inventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        int slot = event.getSlot();
        if (List.of(
                10, 11, 12,
                19, 20, 21, 23,
                28, 29, 30
        ).contains(slot)) {
            event.setCancelled(false);
        } else if (slot == 25) {
            ItemStack result = inventory.getItem(23);
            if (result == null || result.getType() == Material.AIR) {
                return;
            }

            ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, recipeId), result);
            recipe.shape("012", "345", "678");

            List<Integer> slots = List.of(
                    10, 11, 12,
                    19, 20, 21,
                    28, 29, 30
            );
            for (int i = 0; i < 9; i++) {
                int currentSlot = slots.get(i);
                ItemStack itemStack = inventory.getItem(currentSlot);
                if (itemStack == null || itemStack.getType() == Material.AIR) continue;

                recipe.setIngredient(String.valueOf(i).charAt(0), new RecipeChoice.ExactChoice(itemStack));
            }

            recipeRepository.setRecipe(recipeId, recipe);
        }
    }
}