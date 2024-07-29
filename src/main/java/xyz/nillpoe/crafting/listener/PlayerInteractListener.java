package xyz.nillpoe.crafting.listener;

import de.tr7zw.changeme.nbtapi.NBT;
import lombok.AllArgsConstructor;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import xyz.nillpoe.crafting.CustomCrafting;

@AllArgsConstructor
public class PlayerInteractListener implements Listener {

    private final CustomCrafting plugin;

    @EventHandler
    public void onClaim(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();
        if (itemStack == null || itemStack.getType() == Material.AIR) return;

        String recipeId = NBT.get(itemStack, (nbt) -> {
            return nbt.getString("CUSTOM_CRAFTING_CLAIM");
        });
        ShapedRecipe recipe = plugin.getRecipeRepository().getRecipe(recipeId);
        if (recipe == null) return;

        Player player = event.getPlayer();
        User user = plugin.getLuckPerms().getUserManager().getUser(player.getUniqueId());
        if (user == null) return;

        user.data().add(Node.builder("nillpoe.crafting.craft.%s".formatted(recipeId)).build());
        player.sendMessage(ChatColor.GREEN + "지금부터 조합법을 사용하실 수 있습니다.");
    }

    @EventHandler
    public void onUnclaim(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();
        if (itemStack == null || itemStack.getType() == Material.AIR) return;

        String recipeId = NBT.get(itemStack, (nbt) -> {
            return nbt.getString("CUSTOM_CRAFTING_UNCLAIM");
        });
        ShapedRecipe recipe = plugin.getRecipeRepository().getRecipe(recipeId);
        if (recipe == null) return;

        Player player = event.getPlayer();
        User user = plugin.getLuckPerms().getUserManager().getUser(player.getUniqueId());
        if (user == null) return;

        user.data().remove(Node.builder("nillpoe.crafting.craft.%s".formatted(recipeId)).build());
        player.sendMessage(ChatColor.GREEN + "지금부터 조합법을 사용하실 수 있습니다.");
    }
} // 나누는게 나을 듯