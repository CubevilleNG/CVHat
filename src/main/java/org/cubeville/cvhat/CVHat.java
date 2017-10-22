package org.cubeville.cvhat;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class CVHat extends JavaPlugin {

    List<String> allowedBlocks;

    @Override
    public void onEnable() {
        allowedBlocks = getConfig().getStringList("blocks");
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (label.equalsIgnoreCase("hat")) {
            PlayerInventory inventory = player.getInventory();
            ItemStack itemInHand = inventory.getItemInMainHand();
            ItemStack itemInHelmet = inventory.getHelmet();
            if(itemInHelmet.getEnchantmentLevel(Enchantment.BINDING_CURSE) == 0) {
                if (itemInHand.getAmount() == 1) {
                    if (player.hasPermission("cvranks.hat.any") || allowedBlocks.contains(itemInHand.getType() + "")) {
                        inventory.setHelmet(itemInHand);
                        inventory.setItemInMainHand(itemInHelmet);
                    } else {
                        player.sendMessage(ChatColor.RED + "You can't put that on your head.");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Please only hold 1 item!");
                }
            } else {
                player.sendMessage(ChatColor.RED + "You can't take that item off your head!");
            }
            return true;
        }
        return false;
    }
}
