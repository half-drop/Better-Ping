# Better Ping

**A continuation of the beloved Better Ping Display mod, updated and maintained for modern Minecraft.**

This client-side Fabric mod replaces Minecraft's ambiguous ping bars with actual numerical values, so you always know your exact connection quality at a glance.

## Features

- **See Your Real Ping** - Exact millisecond values instead of vague signal bars
- **Smart Color Coding** - Automatic color-coding based on latency (green = smooth, red = laggy)
- **Automatic Placeholder Detection** - Hide both ping numbers and vanilla signal bars when every listed player has the same ping; restore the configured display as soon as their values differ
- **Fully Customizable** - Adjust colors, text format, and display preferences to match your style
- **Works Everywhere** - Client-side only, so it works on any server including vanilla
- **Zero Bloat** - Simple, focused, and lightweight

## Why This Mod?

The original Better Ping Display was abandoned, leaving players without support for newer versions. Better Ping continues that legacy, keeping the mod alive and compatible with the latest Minecraft releases while staying true to its core purpose: giving you clear visibility of your connection.

## Easy Configuration

Customize everything through `betterpingdisplay.json`:
- Toggle automatic color-coding
- Show or hide original ping bars
- Set custom text colors
- Define your own format (e.g., `%dms`, `%d ping`)

## Identical ping detection

This fork automatically checks the full server-provided Tab player list each time the list is rendered. If at least two listed players all have exactly the same latency (for example, everyone shows `10000ms`), both the numeric ping and the vanilla signal bars are hidden, even when `renderPingBars` is enabled. The extra space reserved for numeric ping is also removed.

Detection is recalculated from the current player list, so normal display returns automatically when a player's ping differs. Empty and single-player lists are left unchanged because there is not enough information to compare players. This is a heuristic: genuinely identical pings are also hidden.

## Build

The [Build workflow](../../actions/workflows/build.yml) runs on pushes, pull requests, and manual dispatches. It uses Java 25 to run `./gradlew build`, including the tests, and uploads the mod JAR with `SHA256SUMS.txt` as an Actions artifact.

## Supported Versions

Minecraft 26.3 (Fabric)

**Original Mod:** [Better Ping Display - Fabric](https://modrinth.com/mod/better-ping-display-fabric)

---

*Requires [Fabric Loader](https://fabricmc.net/) and Fabric API*
