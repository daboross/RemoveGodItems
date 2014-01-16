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

import java.util.Map;
import net.daboross.bukkitdev.removegoditems.LogKey;
import net.daboross.bukkitdev.removegoditems.RGICheck;
import net.daboross.bukkitdev.removegoditems.RemoveGodItemsPlugin;
import net.daboross.bukkitdev.removegoditems.SkyLog;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EnchantmentCheck implements RGICheck {

    private final RemoveGodItemsPlugin plugin;

    public EnchantmentCheck(final RemoveGodItemsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void checkItem(final ItemStack itemStack, final Inventory inventory, final Location location, final String playerName) {
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            for (Map.Entry<Enchantment, Integer> entry : itemStack.getEnchantments().entrySet()) {
                Enchantment e = entry.getKey();
                if (entry.getValue() > e.getMaxLevel() || !e.canEnchantItem(itemStack)) {
                    if (plugin.isRemove()) {
                        itemStack.setType(Material.AIR);
                        SkyLog.log(LogKey.REMOVE_OVERENCHANT, itemStack.getType(), e.getName(), entry.getValue(), playerName);
                        return;
                    } else {
                        if (e.canEnchantItem(itemStack)) {
                            SkyLog.log(LogKey.FIX_OVERENCHANT_LEVEL, e.getName(), entry.getValue(), e.getMaxLevel(), itemStack.getType(), playerName);
                            itemStack.addEnchantment(e, e.getMaxLevel());
                        } else {
                            SkyLog.log(LogKey.FIX_OVERENCHANT_REMOVE, e.getName(), entry.getValue(), itemStack.getType(), playerName);
                            itemStack.removeEnchantment(e);
                        }
                    }
                }
            }
        }
    }
}
