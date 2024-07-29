package xyz.nillpoe.crafting.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.nillpoe.crafting.command.sub.*;

import java.util.*;

public class RecipeCommand implements TabExecutor {

    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public RecipeCommand(JavaPlugin plugin) {
        PluginCommand command = plugin.getCommand("조합법");
        if (command != null) {
            command.setExecutor(this);
            registerSubCommands();
        }
    }

    private void registerSubCommands() {
        registerSubCommand(new CreateRecipeCommand());
        registerSubCommand(new ListRecipeCommand());
        registerSubCommand(new DeleteRecipeCommand());
        registerSubCommand(new ClaimItemCommand());
        registerSubCommand(new UnclaimItemCommand());
    }

    private void registerSubCommand(SubCommand subCommand) {
        subCommands.put(subCommand.getName(), subCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GREEN + " 사용 가능한 명령어:");
            subCommands.values().forEach((subCommand) ->
                    sender.sendMessage(ChatColor.WHITE + " /조합법 %s %s ".formatted(subCommand.getName(), subCommand.getUsage()) + ChatColor.GRAY + subCommand.getDescription()));
            sender.sendMessage("");
            return true;
        }

        SubCommand subCmdInstance = subCommands.get(args[0]);
        if (subCmdInstance != null) {
            if (!subCmdInstance.hasPermission(sender)) {
                sender.sendMessage(ChatColor.RED + "명령어를 사용할 권한이 없습니다.");
                return false;
            }

            String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
            return subCmdInstance.execute(sender, label, newArgs);
        }

        sender.sendMessage(ChatColor.RED + "명령어 사용법이 올바르지 않습니다.");
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            for (SubCommand subCommand : subCommands.values()) {
                if (!subCommand.hasPermission(sender)) continue;

                completions.add(subCommand.getName());
            }
        } else {
            SubCommand subCmdInstance = subCommands.get(args[0].toLowerCase());
            if (subCmdInstance != null && subCmdInstance.hasPermission(sender)) {
                completions = subCmdInstance.tabComplete(sender, label, args);
            }
        }

        return StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<>());
    }
}