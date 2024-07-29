package xyz.nillpoe.crafting;

import de.tr7zw.changeme.nbtapi.NBT;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.nillpoe.crafting.command.RecipeCommand;
import xyz.nillpoe.crafting.listener.InventoryListener;
import xyz.nillpoe.crafting.listener.ItemCraftListener;
import xyz.nillpoe.crafting.listener.PlayerInteractListener;
import xyz.nillpoe.crafting.repository.RecipeRepository;

public class CustomCrafting extends JavaPlugin {

    @Getter
    private static CustomCrafting instance;

    @Getter
    private LuckPerms luckPerms;
    @Getter
    private RecipeRepository recipeRepository;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        instance = this;

        // API
        if (!NBT.preloadApi()) {
            getLogger().warning("NBT-API가 정상적이게 로드되지 않았습니다. 플러그인을 비활성화 합니다...");
            getPluginLoader().disablePlugin(this);
            return;
        }

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) luckPerms = provider.getProvider();

        // CONFIG
        saveDefaultConfig();

        // REPOSITORY
        recipeRepository = new RecipeRepository(this);
        recipeRepository.load();

        // EVENT
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new ItemCraftListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);

        // COMMAND
        getCommand("조합법").setExecutor(new RecipeCommand(this));
    }

    @Override
    public void onDisable() {
        recipeRepository.save();
        saveConfig();
    }
}