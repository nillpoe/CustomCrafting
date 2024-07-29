package xyz.nillpoe.crafting.command.sub;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.nillpoe.crafting.command.SubCommand;
import xyz.nillpoe.crafting.inventory.RecipeEditor;

import java.util.Collections;
import java.util.List;

public class CreateRecipeCommand implements SubCommand {

    @Override
    public String getName() {
        return "생성";
    }

    @Override
    public String getDescription() {
        return "조합법을 생성합니다.";
    }

    @Override
    public String getUsage() {
        return "<이름>";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("nillpoe.crafting.create");
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

        new RecipeEditor(args[0])
                .open(player);
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) {
        if (args.length == 1) {
            return List.of("<이름>");
        }

        return Collections.emptyList();
    }
}