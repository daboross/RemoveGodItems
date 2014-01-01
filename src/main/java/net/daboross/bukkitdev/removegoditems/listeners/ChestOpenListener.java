/*
 * Copyright (C) 2013-2014 Dabo Ross <http://www.daboross.net/>
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
package net.daboross.bukkitdev.removegoditems.listeners;

import net.daboross.bukkitdev.removegoditems.GICListener;
import net.daboross.bukkitdev.removegoditems.RemoveGodItemsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class ChestOpenListener implements GICListener {

    private final RemoveGodItemsPlugin plugin;

    public ChestOpenListener(RemoveGodItemsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChestOpen(InventoryOpenEvent evt) {
        plugin.getChecker().runCheckNextTick(evt.getPlayer(), evt.getInventory());
    }

    @Override
    public void unregister() {
        InventoryOpenEvent.getHandlerList().unregister(this);
    }

    @Override
    public void register() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
