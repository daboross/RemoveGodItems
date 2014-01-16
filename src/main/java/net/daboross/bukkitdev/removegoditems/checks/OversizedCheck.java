/*
 * Copyright (C) 2014 Dabo Ross <http://www.daboross.net/>
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
package net.daboross.bukkitdev.removegoditems.checks;

import net.daboross.bukkitdev.removegoditems.LogKey;
import net.daboross.bukkitdev.removegoditems.RGICheck;
import net.daboross.bukkitdev.removegoditems.RemoveGodItemsPlugin;
import net.daboross.bukkitdev.removegoditems.SkyLog;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class OversizedCheck implements RGICheck {

    private final RemoveGodItemsPlugin plugin;

    public OversizedCheck(final RemoveGodItemsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void checkItem(final ItemStack itemStack, final Inventory inventory, final Location location, final String playerName) {
        int maxAmount = itemStack.getType().getMaxStackSize();
        int amount = itemStack.getAmount();
        if (amount > maxAmount) {
            if (plugin.isRemove()) {
                SkyLog.log(LogKey.REMOVE_OVERSTACK, itemStack.getType().name(), amount, playerName);
                itemStack.setType(Material.AIR);
            } else {
                int numStacks = amount / maxAmount;
                int left = amount % maxAmount;
                SkyLog.log(LogKey.FIX_OVERSTACK_UNSTACK, itemStack.getType(), amount, left, numStacks, playerName);
                itemStack.setAmount(left);
                for (int i = 0; i < numStacks; i++) {
                    ItemStack newStack = itemStack.clone();
                    newStack.setAmount(maxAmount);
                    int slot = inventory.firstEmpty();
                    if (slot < 0) {
                        location.getWorld().dropItemNaturally(location, newStack);
                    } else {
                        inventory.setItem(slot, newStack);
                    }
                }
            }
        }
    }
}
