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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class GodItemChecker {

    private final RemoveGodItemsPlugin plugin;

    public GodItemChecker(RemoveGodItemsPlugin plugin) {
        this.plugin = plugin;
    }

    public void removeGodEnchants(HumanEntity player) {
        String name = player.getName();
        PlayerInventory inv = player.getInventory();
        Location loc = player.getLocation();
        for (ItemStack it : inv.getArmorContents()) {
            removeGodEnchants(it, inv, loc, name);
        }
        for (ItemStack it : inv.getContents()) {
            removeGodEnchants(it, inv, loc, name);
        }
    }

    public void removeGodEnchants(ItemStack itemStack, HumanEntity p) {
        removeGodEnchants(itemStack, p.getInventory(), p.getLocation(), p.getName());
    }

    public void removeGodEnchants(ItemStack itemStack, Inventory inv, Location loc, String name) {
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            for (Map.Entry<Enchantment, Integer> entry : itemStack.getEnchantments().entrySet()) {
                Enchantment e = entry.getKey();
                if (entry.getValue() > e.getMaxLevel() || !e.canEnchantItem(itemStack)) {
                    if (plugin.isRemove()) {
                        itemStack.setType(Material.AIR);
                        SkyLog.log(LogKey.REMOVE_OVERENCHANT, itemStack.getType(), e.getName(), entry.getValue(), name);
                        return;
                    } else {
                        if (e.canEnchantItem(itemStack)) {
                            SkyLog.log(LogKey.FIX_OVERENCHANT_LEVEL, e.getName(), entry.getValue(), e.getMaxLevel(), itemStack.getType(), name);
                            itemStack.addEnchantment(e, e.getMaxLevel());
                        } else {
                            SkyLog.log(LogKey.FIX_OVERENCHANT_REMOVE, e.getName(), entry.getValue(), itemStack.getType(), name);
                            itemStack.removeEnchantment(e);
                        }
                    }
                }
            }
            checkOverstack(itemStack, inv, loc, name);
        }
    }

    public void checkOverstack(ItemStack itemStack, Inventory inv, Location loc, String name) {
        int maxAmount = itemStack.getType().getMaxStackSize();
        int amount = itemStack.getAmount();
        if (amount > maxAmount) {
            if (plugin.isRemove()) {
                SkyLog.log(LogKey.REMOVE_OVERSTACK, itemStack.getType().name(), amount, name);
                itemStack.setType(Material.AIR);
            } else {
                int numStacks = amount / maxAmount;
                int left = amount % maxAmount;
                SkyLog.log(LogKey.FIX_OVERSTACK_UNSTACK, itemStack.getType(), amount, left, numStacks, name);
                itemStack.setAmount(left);
                for (int i = 0; i < numStacks; i++) {
                    ItemStack newStack = itemStack.clone();
                    newStack.setAmount(maxAmount);
                    int slot = inv.firstEmpty();
                    if (slot < 0) {
                        loc.getWorld().dropItemNaturally(loc, newStack);
                    } else {
                        inv.setItem(slot, newStack);
                    }
                }
            }
        }
    }

    public void runFullCheckNextSecond(Player p) {
        plugin.getServer().getScheduler().runTaskLater(plugin, new GodItemFixRunnable(p), 20);
    }

    public void removeGodEnchantsNextTick(HumanEntity p, Iterable<Integer> slots) {
        plugin.getServer().getScheduler().runTask(plugin, new VariedCheckRunnable(p, slots));
    }

    public void runCheckNextTick(HumanEntity p, Inventory i) {
        plugin.getServer().getScheduler().runTask(plugin, new InventoryCheckRunnable(p.getName(), i, p.getLocation()));
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

    public class VariedCheckRunnable implements Runnable {

        private final HumanEntity p;
        private final Iterable<Integer> items;

        public VariedCheckRunnable(HumanEntity p, Iterable<Integer> items) {
            this.p = p;
            this.items = items;
        }

        @Override
        public void run() {
            String name = p.getName();
            Inventory inv = p.getInventory();
            int size = inv.getSize();
            for (Integer i : items) {
                if (i > 0 && i < size) {
                    removeGodEnchants(inv.getItem(i), inv, p.getLocation(), name);
                }
            }
        }
    }

    public class InventoryCheckRunnable implements Runnable {

        private final String name;
        private final Inventory inv;
        private final Location location;

        public InventoryCheckRunnable(final String name, final Inventory inv, final Location location) {
            this.name = name;
            this.inv = inv;
            this.location = location;
        }

        @Override
        public void run() {
            for (int i = 0; i < inv.getSize(); i++) {
                removeGodEnchants(inv.getItem(i), inv, location, name);
            }
        }
    }
}
