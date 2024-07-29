package xyz.nillpoe.crafting.command.sub;

import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.nillpoe.crafting.CustomCrafting;
import xyz.nillpoe.crafting.command.SubCommand;
import xyz.nillpoe.crafting.repository.RecipeRepository;

import java.util.ArrayList;
import java.util.List;

public class ClaimItemCommand implements SubCommand {

    @Override
    public String getName() {
        return "등록아이템";
    }

    @Override
    public String getDescription() {
        return "손에 들고 있는 아이템을 조합법을 사용할 수 있는 권한을 지급받는 아이템으로 설정합니다.";
    }

    @Override
    public String getUsage() {
        return "<이름>";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("nillpoe.crafting.claimitem");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "해당 명령어는 플레이어만 사용할 수 있습니다.");
            return false;
        } else if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "생성할 조합법의 이름을 입력해 주세요.");
            return false;
        } else if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "명령어 사용법이 올바르지 않습니다.");
            return false;
        }

        String recipeId = args[0];

        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            sender.sendMessage(ChatColor.RED + "손에 아이템을 들고 다시 시도해 주세요.");
            return false;
        }

        NBT.modify(itemStack, (nbt) -> {
            nbt.setString("CUSTOM_CRAFTING_CLAIM", recipeId);
        });

        sender.sendMessage(ChatColor.GREEN + "성공적으로 아이템을 설정했습니다.");
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            CustomCrafting plugin = CustomCrafting.getInstance();
            RecipeRepository recipeRepository = plugin.getRecipeRepository();

            completions.add("<이름>");
            completions.addAll(recipeRepository.getRecipes().stream()
                    .map((recipe) -> recipe.getKey().getKey())
                    .toList());
        }

        return completions;
    }
}