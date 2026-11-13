# Icy Combat Controls

Forge 1.20.1 compatibility addon for Horse Combat Controls and Icy's Better Horses, with optional Supplementaries and Epic Knights support.

## Movement ownership

- `CombatMode` detects a Better Horses horse controlled with Horse Combat Controls combat mode.
- The client and server horse mixins provide the same accelerating throttle and independent turning on both sides.
- Better Horses gait selection and auto-drive are paused while combat controls own movement.
- Rearing, jumping, charge bashing, breed abilities, equipment, and carts remain Better Horses features.

## Charge initialization

Better Horses sends the player's charge preference during an early login event. At that point the client connection can still be unavailable. `ClientHorseEvents` resends the player's actual preference after the local player mounts a managed horse, when the connection is ready. It does not force charging on if the player disabled it.

## Mounting and dismounting safety

`HorseInteractionEvents` uses Forge's mount event for two fixes:

- A player's inherited velocity is cleared when dismounting any horse, preventing the Horse Combat Controls momentum launch.
- Horses are prevented from becoming passengers of chairs, mobs, vehicles, or other modded entities.

The addon's common config also provides three independent, default-on control fixes:

- `face_horse_direction_on_mount`: preserves the horse's horizontal direction and turns the rider to face it; pitch is unchanged.
- `reset_throttle_on_mount_or_dismount`: clears both the addon and Horse Combat Controls accelerating throttles on mount and dismount.
- `reset_throttle_on_rear`: clears both throttles when the horse enters its rearing pose, including Better Horses' rear command.

No standing, rearing, sitting, or other horse pose is forced. Only the actual mount operation is blocked.

## Better Horses stat ranges

Forge creates `world/serverconfig/icy_combat_controls-server.toml` for the stat settings and syncs them to clients. Every one of Better Horses' 15 breeds has editable minimum and maximum values for health, movement speed, and jump strength under `better_horses_stats.breeds`. `other_horses` covers managed horses without one of those breed keys. Health is in points (two points per heart); speed and jump strength use Minecraft's internal attribute values.

The defaults match Better Horses' natural breed ranges. The three race breeds can reach speed `0.3472` and jump `1.02`, slightly above the vanilla `0.3375` and `1.0` limits. War horses can have up to 40 health, drafts up to 50, and ponies up to 40; these are deliberate breed differences. Lowering any breed's maximum gives it a stricter ceiling, while raising it permits stronger bond bonuses.

Newly spawned breed horses are rolled across the configured range, preserving variation between individuals. Bred foals and converted horses use the configured bounds. Existing horses retain their saved natural rolls, but their effective stats are capped by the configured maximum, including bond and ability modifiers. The minimum does not reroll existing horses. Horse Combat Controls continues to handle steering and throttle; it uses the capped horse attributes for movement and jumping.

## Optional Supplementaries encumbrance

When Supplementaries is installed, the addon attaches a Better Horses feature that checks physical horse cargo every 27 server ticks. It counts filled sacks and Supplementaries `overencumbering` items in the normal horse chest and, when enabled, an attached storage cart. Ender Chest gear is excluded.

It applies Supplementaries' real Overencumbered effect with Supplementaries' threshold, duration, amplifier, particles, and icon behavior. Forge creates `config/icy_combat_controls-common.toml` with these addon settings:

- `horse_encumbrance`: master switch; default `true`.
- `horse_encumbrance_multiplier`: scales horse sack weight; default `1.0`.
- `include_cart_storage`: include an attached storage cart; default `true`.
- `only_while_ridden`: only check ridden horses; default `false`.

Supplementaries' own sack settings remain authoritative.

## Optional Epic Knights decorated horse armor

Epic Knights attaches its caparison/banner-pattern layer only to vanilla `HorseRenderer` instances. Better Horses uses custom renderers, so Epic Knights armor itself works but its banner decoration layer is skipped.

`EpicKnightsClientCompat` attaches an equivalent layer to every Better Horses renderer. It:

1. Reads the banner data stored on the equipped horse armor.
2. Uses Epic Knights' own caparison and heraldry textures.
3. Fits the cloth mesh to the current Better Horses breed or foal body.
4. Renders the cloth as a child of the live Better Horses torso, keeping it attached through cornering tilt and custom animations.
5. Honors Better Horses' first-person horse transparency.

Epic Knights remains optional and none of its Java classes are linked by the addon.

yes i generated this segment with ai because i am a programmer and i dont code so i have to write documentation later
