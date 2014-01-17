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

import com.comphenix.attribute.Attributes;
import net.daboross.bukkitdev.removegoditems.LogKey;
import net.daboross.bukkitdev.removegoditems.RGICheck;
import net.daboross.bukkitdev.removegoditems.RemoveGodItemsPlugin;
import net.daboross.bukkitdev.removegoditems.SkyLog;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AttributesCheck implements RGICheck {

    private final RemoveGodItemsPlugin plugin;

    public AttributesCheck(final RemoveGodItemsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void checkItem(final ItemStack itemStack, final Inventory playerInventory, final Location playerLocation, final String playerName) {
        Attributes attributes = new Attributes(itemStack);
        for (Attributes.Attribute attribute : attributes.values()) {
            if (attribute.getAmount() > 10) {
                if (plugin.isRemove()) {
                    SkyLog.log(LogKey.REMOVE_ATTRIBUTE, itemStack.getType(), attribute.getAttributeType(), attribute.getAmount(), playerName);
                    itemStack.setType(Material.AIR);
                    return;
                } else {
                    SkyLog.log(LogKey.FIX_ATTRIBUTE, attribute.getAttributeType(), attribute.getAmount(), itemStack.getType(), playerName);
                    attributes.remove(attribute);
                }
            }
        }
    }
}
