/*
 * Copyright (C) 2013 Dabo Ross <http://www.daboross.net/>
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
import org.bukkit.inventory.meta.ItemMeta;

public class NameLengthCheck implements RGICheck {

    private final RemoveGodItemsPlugin plugin;

    public NameLengthCheck(final RemoveGodItemsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void checkItem(final ItemStack itemStack, final Inventory inventory, final Location location, final String playerName) {
        String name = itemStack.getItemMeta().getDisplayName();
        if (name.length() > 32) {
            if (plugin.isRemove()) {
                SkyLog.log(LogKey.REMOVE_OVERLENGTH, itemStack.getType(), name.length(), playerName);
                itemStack.setType(Material.AIR);
                return;
            }
            SkyLog.log(LogKey.FIX_OVERLENGTH, name.length(), itemStack.getType(), playerName);
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(name.substring(0, 31));
            itemStack.setItemMeta(meta);
        }
    }
}
