package me.stephenhendricks.potionstacker;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PotStackCommandHandler implements CommandExecutor {
    private PotionStacker plugin;

    public PotStackCommandHandler(PotionStacker plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be run by a player");
            return false;
        }

        int emptySlots = 0;
        Player player = (Player) sender;
        ItemStack[] storageContents = player.getInventory().getStorageContents();
        Map<ItemStack, Integer> potionAmounts = new HashMap<>();

        for (ItemStack stack : storageContents) {
            if (stack == null || stack.getType() == Material.AIR) {
                emptySlots++;
                continue;
            }

            if (stack.getType() == Material.POTION) {
                emptySlots++;
                ItemStack cloned = stack.clone();
                cloned.setAmount(1);

                if (potionAmounts.containsKey(cloned)) {
                    potionAmounts.put(cloned, potionAmounts.get(cloned) + stack.getAmount());
                } else {
                    potionAmounts.put(cloned, stack.getAmount());
                }
            }
        }

        ArrayList<ItemStack> potionStacks = new ArrayList<>();

        for (Map.Entry<ItemStack, Integer> entry : potionAmounts.entrySet()) {
            ItemStack stack = entry.getKey();
            int amount = entry.getValue();
            int maxStackSize = this.plugin.getConfig().getInt("stackSize." + ((PotionMeta) stack.getItemMeta()).getBasePotionData().getType().toString());

            if (amount <= maxStackSize) {
                stack.setAmount(amount);
                potionStacks.add(new ItemStack(stack));
            } else {
                while (amount >= maxStackSize) {
                    stack.setAmount(maxStackSize);
                    potionStacks.add(new ItemStack(stack));
                    amount -= maxStackSize;
                }
                if (amount > 0) {
                    stack.setAmount(amount);
                    potionStacks.add(new ItemStack(stack));
                }
            }
        }

        if (potionStacks.size() > emptySlots) {
            player.sendMessage(ChatColor.RED + "You don't have enough free space in your inventory.");
            return false;
        }

        player.getInventory().remove(Material.POTION);
        player.getInventory().addItem(potionStacks.toArray(new ItemStack[0]));
        player.sendMessage(ChatColor.GREEN + ChatColor.translateAlternateColorCodes('&', this.plugin.getPrefix()) + " Your potions have been stacked!");
        return true;
    }
}
