package xyz.nillpoe.crafting.command.sub;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import xyz.nillpoe.crafting.CustomCrafting;
import xyz.nillpoe.crafting.command.SubCommand;
import xyz.nillpoe.crafting.repository.RecipeRepository;

import java.util.Collections;
import java.util.List;

public class ListRecipeCommand implements SubCommand {

    @Override
    public String getName() {
        return "목록";
    }

    @Override
    public String getDescription() {
        return "조합 가능한 조합법의 목록을 확인합니다.";
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("nillpoe.crafting.list");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length != 0) {
            sender.sendMessage(ChatColor.RED + "명령어 사용법이 올바르지 않습니다.");
            return false;
        }

        CustomCrafting plugin = CustomCrafting.getInstance();
        RecipeRepository recipeRepository = plugin.getRecipeRepository();

        List<String> availableRecipes = recipeRepository.getRecipes().stream()
                .map((recipe) -> recipe.getKey().getKey())
                .filter((recipeId) -> sender.hasPermission("nillpoe.crafting.craft.%s".formatted(recipeId)))
                .toList();

        sender.sendMessage(ChatColor.GREEN + "사용 가능한 조합법:");
        sender.sendMessage(ChatColor.WHITE + String.join(", ", availableRecipes));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) {
        return Collections.emptyList();
    }
}