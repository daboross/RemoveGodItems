RemoveGodItems - Stop cheating players
======================================

Features
========
Removes all unattainable enchantments from inventories of players at the following times:
* When a creative player gets a new item
* When a player joins
* Shortly after a player changes worlds

Also logs all changes to console

Configuration
=============
config.yml
```
# Should god items be removed instead of 'fixed'?
remove-items: false
```
Basically, set remove-items to true if you want items to be completely removed if they are invalid. When false, items will have all enchantment levels reduced to normal.

Commands
========
* /rgireload
 * Permission is `removegoditems.reload`
 * Reloads the configuration.

Useful Links
============
* [Github source](https://github.com/daboross/RemoveGodItems)
* [Github Wiki](https://github.com/daboross/RemoveGodItems/wiki)

Development Builds
==================

* [Development builds](http://ci.dabo.guru/p/BukkitPlugins_RemoveGodItems)
* [Download latest development build](http://ci.dabo.guru/d/BukkitPlugins_RemoveGodItems_MainBuild/RemoveGodItems.jar)
