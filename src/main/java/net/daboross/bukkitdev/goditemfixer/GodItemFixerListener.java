/*
 * Author: Dabo Ross (Based off of MasterGabeMod's work)
 * Website: www.daboross.net
 * Email: daboross@daboross.net
 */
package net.daboross.bukkitdev.goditemfixer;

import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author daboross (based off of MasterGabeMod's work)
 */
public class GodItemFixerListener implements Listener {

    private final Plugin plugin;

    public GodItemFixerListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent evt) {
        removeGodEnchants(evt.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldChange(PlayerChangedWorldEvent evt) {
        Bukkit.getScheduler().runTaskLater(plugin, new GodItemFixRunnable(evt.getPlayer()), 20);
    }

    public static void removeGodEnchants(Player p) {
        for (ItemStack it : p.getInventory().getArmorContents()) {
            removeGodEnchants(it, p);
        }
        for (ItemStack it : p.getInventory().getContents()) {
            removeGodEnchants(it, p);
        }
    }

    public static void removeGodEnchants(ItemStack it, Player p) {
        if (it != null && it.getEnchantments().size() > 0 && it.getType() != Material.AIR) {
            for (Map.Entry<Enchantment, Integer> entry : it.getEnchantments().entrySet()) {
                Enchantment e = entry.getKey();
                if (entry.getValue() > e.getMaxLevel() || !e.canEnchantItem(it)) {
                    String message;
                    if (e.canEnchantItem(it)) {
                        message = String.format("Changed level of enchantment %s from %s to %s on item %s in inventory of %s", e.getName(), entry.getValue(), e.getMaxLevel(), it.getType().toString(), p.getName());
                        it.addEnchantment(e, e.getMaxLevel());
                    } else {
                        message = String.format("Removed enchantment %s level %s on item %s in inventory of %s", e.getName(), entry.getValue(), it.getType().toString(), p.getName());
                        it.removeEnchantment(e);
                    }
                    Bukkit.getLogger().log(Level.INFO, "[GodItemFix] {0}", message);
                }
            }
        }
    }

    private static class GodItemFixRunnable implements Runnable {

        private final Player p;

        private GodItemFixRunnable(Player p) {
            this.p = p;
        }

        @Override
        public void run() {
            removeGodEnchants(p);
        }
    }
}