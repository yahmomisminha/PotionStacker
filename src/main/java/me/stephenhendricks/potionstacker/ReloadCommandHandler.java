package me.stephenhendricks.potionstacker;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommandHandler implements CommandExecutor {
    private PotionStacker plugin;

    public ReloadCommandHandler(PotionStacker plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.plugin.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + ChatColor.translateAlternateColorCodes('&', this.plugin.getPrefix()) + " Configuration Successfully Reloaded!");
        return true;
    }
}
