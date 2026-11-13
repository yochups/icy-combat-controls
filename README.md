# Icy Combat Controls

i made this so Horse Combat Controls and Icy's Better Horses work together on Forge 1.20.1.

- combat mode steering and acceleration work with Better Horses
- getting off a horse no longer launches you
- mounting keeps the horse facing the same way
- acceleration resets when you mount, dismount, or rear
- horses can't ride chairs or other entities

The mount direction and acceleration options are in `config/icy_combat_controls-common.toml`.

## Bond and stats

Better Horses' bond bonus can make a horse jump over eight blocks. i added minimum and maximum health, speed, and jump strength for all 15 breeds. Horses still spawn with different stats, but bond and other bonuses stop at the breed's maximum.

The defaults keep the breeds different. Existing horses keep their original rolled stats, but the maximum still applies.

Change the ranges in `world/serverconfig/icy_combat_controls-server.toml` under `better_horses_stats.breeds`. Health is in points (30 is 15 hearts). Speed and jump strength use Minecraft's internal values.

## Optional mods

Supplementaries: horses can get Overencumbered from carrying too many sacks. Its options are in the common config.

Epic Knights: banner patterns show on Better Horses armor.
