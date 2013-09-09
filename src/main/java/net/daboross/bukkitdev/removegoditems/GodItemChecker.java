/*
 * Copyright (C) 2013 daboross
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.daboross.bukkitdev.removegoditems;

import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author daboross
 */
public class GodItemChecker {

    private final RemoveGodItemsPlugin plugin;

    public GodItemChecker(RemoveGodItemsPlugin plugin) {
        this.plugin = plugin;
    }

    public void removeGodEnchants(HumanEntity player) {
        Inventory inv = player.getInventory();
        Location loc = player.getLocation();
        String name = player.getName();
        for (ItemStack it : player.getInventory().getArmorContents()) {
            removeGodEnchants(it, inv, loc, name);
        }
        for (ItemStack it : player.getInventory().getContents()) {
            removeGodEnchants(it, inv, loc, name);
        }
    }

    public void removeGodEnchants(ItemStack itemStack, HumanEntity p) {
        removeGodEnchants(itemStack, p.getInventory(), p.getLocation(), p.getName());
    }

    public void removeGodEnchants(ItemStack itemStack, Inventory inventory, Location location, String name) {
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            for (Map.Entry<Enchantment, Integer> entry : itemStack.getEnchantments().entrySet()) {
                Enchantment e = entry.getKey();
                if (entry.getValue() > e.getMaxLevel() || !e.canEnchantItem(itemStack)) {
                    itemStack.setType(Material.AIR);
                    plugin.getLogger().log(Level.INFO, String.format("Removed item %s with %s level %s from %s", itemStack.getType(), e.getName(), entry.getValue(), name));
                }
            }
            checkOverstack(itemStack, inventory, location, name);
        }
    }

    public void checkOverstack(ItemStack itemStack, Inventory inventory, Location location, String name) {
        int maxAmount = itemStack.getType().getMaxStackSize();
        int amount = itemStack.getAmount();
        if (amount > maxAmount) {
            plugin.getLogger().log(Level.INFO, "Removed overstacked item {0} of size {1} from {2}", new Object[]{itemStack.getType().name(), amount, name});
            itemStack.setType(Material.AIR);
        }
    }

    public void runFullCheckNextSecond(Player p) {
        Bukkit.getScheduler().runTaskLater(plugin, new GodItemFixRunnable(p), 20);
    }

    public void removeGodEnchantsNextTick(HumanEntity p, Iterable<Integer> slots) {
        Bukkit.getScheduler().runTask(plugin, new VarriedCheckRunnable(p, slots));
    }

    public class GodItemFixRunnable implements Runnable {

        private final HumanEntity p;

        public GodItemFixRunnable(HumanEntity p) {
            this.p = p;
        }

        @Override
        public void run() {
            removeGodEnchants(p);
        }
    }

    public class VarriedCheckRunnable implements Runnable {

        private final HumanEntity p;
        private final Iterable<Integer> items;

        public VarriedCheckRunnable(HumanEntity p, Iterable<Integer> items) {
            this.p = p;
            this.items = items;
        }

        @Override
        public void run() {
            Inventory inv = p.getInventory();
            Location loc = p.getLocation();
            String name = p.getName();
            for (Integer i : items) {
                removeGodEnchants(inv.getItem(i), inv, loc, name);
            }
        }
    }
}
