package me.stephenhendricks.potionstacker;

import org.bukkit.plugin.java.JavaPlugin;

public final class PotionStacker extends JavaPlugin {

    private String prefix;

    /**
     * return the prefix string for console messages
     *
     * @return
     */
    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public void onEnable() {
        // load config.yml into memory
        saveDefaultConfig();

        //store the prefix for console messages
        this.prefix = getConfig().getString("prefix");

        //register commands
        getServer().getPluginCommand("pot").setExecutor(new PotStackCommandHandler(this));
        getServer().getPluginCommand("potreload").setExecutor(new ReloadCommandHandler(this));
    }
}
