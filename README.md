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

No standing, rearing, sitting, or other horse pose is forced. Only the actual mount operation is blocked.

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
