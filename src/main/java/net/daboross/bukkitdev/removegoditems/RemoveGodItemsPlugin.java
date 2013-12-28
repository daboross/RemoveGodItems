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
import java.util.logging.Level;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

public class RemoveGodItemsPlugin extends JavaPlugin {

    private GodItemChecker checker;

    @Override
    public void onEnable() {
        checker = new GodItemChecker(this);
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new RemoveGodItemsListener(this), this);
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
    }

    public GodItemChecker getChecker() {
        return checker;
    }
}
