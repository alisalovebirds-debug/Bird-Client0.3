# Bird Client — Minecraft 1.21.11 Fabric

A lightweight client-side GUI prototype inspired by the supplied reference image.

## Included
- Right Shift opens/closes the Bird Client menu.
- Escape closes it.
- Bird Client branding with bird emoji in the top-left.
- Top tabs: Modules / Favorites / Appearance.
- Sidebar: Combat, Utility, Movement, Misc, Tools, Visual.
- General section: Settings, Theme, Configs, Socials, Keybinds.
- Clickable module cards with working on/off toggles.
- Dark blue/purple interface with rounded-looking layered panels.
- Bottom-right text: `Bird Client 1.21.11`.
- Client screen does not pause the game.

## Build

Requires Java 21 and Gradle. In a normal desktop development environment:

```text
./gradlew build
```

The finished mod jar will be in `build/libs/`.

For Minecraft 1.21.11, use Fabric Loader and Fabric API matching 1.21.11. The project is configured for Loader 0.18.1, Yarn 1.21.11+build.6, and Fabric API 0.141.6+1.21.11.

## Install

Put the built `bird-client-1.0.0.jar` in your Fabric `mods` folder alongside Fabric API.

This is a UI/client framework; the module names are intentionally harmless placeholders. Weapon-specific functionality is not included.
