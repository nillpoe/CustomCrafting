package xyz.nillpoe.crafting.inventory;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import xyz.nillpoe.crafting.CustomCrafting;
import xyz.nillpoe.crafting.repository.RecipeRepository;

public abstract class BaseInventory implements InventoryHolder {

    protected final CustomCrafting plugin = CustomCrafting.getInstance();
    protected final RecipeRepository recipeRepository = plugin.getRecipeRepository();

    @Getter
    protected Inventory inventory;
    @Getter
    protected String title;
    @Getter
    protected int rows;

    protected boolean cancelClick;

    public BaseInventory(String title, int rows, boolean cancelClick) {
        this.title = title;
        this.rows = rows;
        this.cancelClick = cancelClick;
    }

    public boolean isClickCancelling() {
        return cancelClick;
    }

    protected void setClickCancelling(boolean cancelClick) {
        this.cancelClick = cancelClick;
    }

    public void open(Player player) {
        if (player != null && player.isOnline()) {
            player.closeInventory();

            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                createInventory(player);
                player.openInventory(inventory);
            }, 1L);
        }
    }

    protected void createInventory(Player player) {
        inventory = plugin.getServer().createInventory(this, rows * 9, title);

        initializeInventory(inventory, player);
    }

    protected void clearInventory(Inventory inventory) {
        for (int i = 0; i < rows * 9; i++) {
            inventory.setItem(i, null);
        }
    }

    public void updateInventory(Player player) {
        clearInventory(inventory);
        initializeInventory(inventory, player);
    }

    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;

        if (cancelClick)
            event.setCancelled(true);
        if (event.getClickedInventory().equals(inventory))
            inventoryClick(event);
    }

    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().equals(inventory))
            inventoryClose(event);
    }

    protected abstract void initializeInventory(Inventory inventory, Player player);
    protected void inventoryClick(InventoryClickEvent event) {}
    protected void inventoryClose(InventoryCloseEvent event) {}
}