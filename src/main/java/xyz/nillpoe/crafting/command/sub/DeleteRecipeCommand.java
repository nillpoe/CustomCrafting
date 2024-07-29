package xyz.nillpoe.crafting.command.sub;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import xyz.nillpoe.crafting.CustomCrafting;
import xyz.nillpoe.crafting.command.SubCommand;
import xyz.nillpoe.crafting.repository.RecipeRepository;

import java.util.ArrayList;
import java.util.List;

public class DeleteRecipeCommand implements SubCommand {

    @Override
    public String getName() {
        return "삭제";
    }

    @Override
    public String getDescription() {
        return "조합법을 영구적으로 삭제합니다.";
    }

    @Override
    public String getUsage() {
        return "<이름>";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("nillpoe.crafting.delete");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "삭제할 조합법의 이름을 입력해 주세요.");
            return false;
        } else if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "명령어 사용법이 올바르지 않습니다.");
            return false;
        }

        String recipeId = args[0];

        CustomCrafting plugin = CustomCrafting.getInstance();
        RecipeRepository recipeRepository = plugin.getRecipeRepository();

        recipeRepository.deleteRecipe(recipeId);
        sender.sendMessage(ChatColor.GREEN + "조합법을 성공적으로 삭제했습니다.");
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