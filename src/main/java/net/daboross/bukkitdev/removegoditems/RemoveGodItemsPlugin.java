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
package net.daboross.bukkitdev.removegoditems;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import net.daboross.bukkitdev.removegoditems.listeners.ChestOpenListener;
import net.daboross.bukkitdev.removegoditems.listeners.CreativeInventoryListener;
import net.daboross.bukkitdev.removegoditems.listeners.InventoryMoveListener;
import net.daboross.bukkitdev.removegoditems.listeners.ItemPickupListener;
import net.daboross.bukkitdev.removegoditems.listeners.JoinListener;
import net.daboross.bukkitdev.removegoditems.listeners.WorldChangeListener;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

public class RemoveGodItemsPlugin extends JavaPlugin {

    private GodItemChecker checker;
    private GICListener[] listeners;
    private boolean remove;

    @Override
    public void onEnable() {
        SkyLog.setLogger(getLogger());
        saveDefaultConfig();
        loadConfiguration();
        checker = new GodItemChecker(this);
        MetricsLite metrics = null;
        try {
            metrics = new MetricsLite(this);
        } catch (IOException ex) {
            getLogger().log(Level.WARNING, "Unable to create Metrics: {0}", ex.toString());
        }
        if (metrics != null) {
            metrics.start();
        }
    }

    @Override
    public void onDisable() {
        SkyLog.setLogger(null);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        reloadConfig();
        unloadListeners();
        loadConfiguration();
        sender.sendMessage(ChatColor.DARK_GRAY + "Configuration reloaded.");
        return true;
    }

    private void unloadListeners() {
        for (GICListener listener : listeners) {
            listener.unregister();
        }
    }

    private void loadConfiguration() {
        remove = getConfig().getBoolean("remove-items");
        List<String> listenerNames = getConfig().getStringList("listeners");
        listeners = new GICListener[listenerNames.size()];
        for (int i = 0; i < listenerNames.size(); i++) {
            String listenerName = listenerNames.get(i).toLowerCase();
            GICListener listener;
            if (listenerName.equals("creative-inventory")) {
                listener = new CreativeInventoryListener(this);
            } else if (listenerName.equals("inventory-move")) {
                listener = new InventoryMoveListener(this);
            } else if (listenerName.equals("item-pickup")) {
                listener = new ItemPickupListener(this);
            } else if (listenerName.equals("join")) {
                listener = new JoinListener(this);
            } else if (listenerName.equals("world-change")) {
                listener = new WorldChangeListener(this);
            } else if (listenerName.equals("chest-open")) {
                listener = new ChestOpenListener(this);
            } else {
                getLogger().log(Level.WARNING, "Unknown listener ''{0}''.", listenerName);
                continue;
            }
            listeners[i] = listener;
            listener.register();
        }
    }

    public GodItemChecker getChecker() {
        return checker;
    }

    public boolean isRemove() {
        return remove;
    }
}
