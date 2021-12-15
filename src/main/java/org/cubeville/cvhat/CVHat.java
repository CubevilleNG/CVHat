package org.cubeville.cvhat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class CVHat extends JavaPlugin implements Listener {

    private Map<String, List<String>> allowedBlocks;
    
    @Override
    public void onEnable() {
    	this.saveDefaultConfig();
	allowedBlocks = new HashMap<>();
	Set<String> permissions = getConfig().getConfigurationSection("blocks").getKeys(false);
	for(String permission: permissions) {
	    allowedBlocks.put(permission, getConfig().getConfigurationSection("blocks").getStringList(permission));
	}
	// old: allowedBlocks = getConfig().getStringList("blocks");
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!label.equals("hat")) {
            return false;
        }
        
        Player player = (Player) sender;
        
        PlayerInventory inventory = player.getInventory();
        ItemStack itemInHand = inventory.getItemInMainHand();
        ItemStack itemInHelmet = inventory.getHelmet();

        if(itemInHelmet != null && itemInHelmet.getEnchantmentLevel(Enchantment.BINDING_CURSE) > 0) {
            player.sendMessage(ChatColor.RED + "You can't take that item off your head!");
            return true;
        }

        if(itemInHand.getAmount() == 0) {
            player.sendMessage(ChatColor.RED + "Please hold the item in your hand!");
            return true;
        }

        if(itemInHand.getAmount() > 1) {
            player.sendMessage(ChatColor.RED + "Please only hold one item!");
            return true;       
        }

	String itemName = itemInHand.getType().toString();

	boolean hasPermission = false;
	if(player.hasPermission("cvhat.hat.any")) {
	    hasPermission = true;
	}
	else {
	    for(String permission: allowedBlocks.keySet()) {
		if(player.hasPermission("cvhat.hat." + permission) && allowedBlocks.get(permission).contains(itemName)) {
		    hasPermission = true;
		    break;
		}
	    }
	}

        // if(player.hasPermission("cvhat.hat.any") == false &&
        //    allowedBlocks.contains(itemInHand.getType().toString()) == false) {
        //     player.sendMessage(ChatColor.RED + "This item can't be put on your head!");
        //     return true;
        // }

	if(!hasPermission) {
	    player.sendMessage(ChatColor.RED + "This item can't be put on your head!");
	    return true;
	}

        inventory.setHelmet(itemInHand);
        inventory.setItemInMainHand(itemInHelmet);

        return true;
    }
    
 }
