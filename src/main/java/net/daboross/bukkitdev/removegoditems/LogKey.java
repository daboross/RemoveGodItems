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

public enum LogKey {
    REMOVE_OVERSTACK("Removed overstacked item %s of size %s from %s"),
    REMOVE_OVERENCHANT("Removed item %s with %s level %s from %s"),
    REMOVE_ATTRIBUTE("Removed item %s with attribute %s level %s from %s"),
    FIX_OVERENCHANT_REMOVE("Removed enchantment %s level %s on item %s in inventory of %s"),
    FIX_OVERENCHANT_LEVEL("Changed level of enchantment %s from %s to %s on item %s in inventory of %s"),
    FIX_OVERSTACK_UNSTACK("Unstacked item %s of size %s to size %s with %s extra stacks of size %s in inventory of %s"),
    FIX_ATTRIBUTE("Removed attribute %s level %s from %s in inventory of %s");
    public final String message;

    LogKey(final String message) {
        this.message = message;
    }
}
