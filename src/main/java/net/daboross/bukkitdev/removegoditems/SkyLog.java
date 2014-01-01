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
package net.daboross.bukkitdev.removegoditems;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;

public class SkyLog {

    private static final Logger defaultLogger = Bukkit.getLogger();
    private static Logger logger = defaultLogger;

    static void setLogger(Logger logger) {
        SkyLog.logger = logger == null ? defaultLogger : logger;
    }

    public static void log(LogKey key, Object... args) {
        if (logger == defaultLogger) {
            logger.log(Level.INFO, String.format("[RemoveGodItems] " + key.message, args));
        } else {
            logger.log(Level.INFO, String.format(key.message, args));
        }
    }
}
