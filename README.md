# Better Ping

**A continuation of the beloved Better Ping Display mod, updated and maintained for modern Minecraft.**

This client-side Fabric mod replaces Minecraft's ambiguous ping bars with actual numerical values, so you always know your exact connection quality at a glance.

## Features

- **See Your Real Ping** - Exact millisecond values instead of vague signal bars
- **Smart Color Coding** - Automatic color-coding based on latency (green = smooth, red = laggy)
- **Automatic Placeholder Detection** - Hide both ping numbers and vanilla signal bars when displayed players share a ping or a custom Tab layout uses repeated placeholder values
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

When `renderPingBars` is `false`, the numeric ping is right-aligned with a two-pixel margin inside each player column. Space for the vanilla signal icon is reserved only when the icon is enabled.

## Placeholder ping detection

This fork checks exactly the sorted player entries used to render the current Tab list (up to vanilla's 80-entry limit). Extra entries outside that visible list cannot prevent detection. Both numeric ping and vanilla signal bars are hidden, even when `renderPingBars` is enabled, in either of these cases:

- At least two displayed entries all have exactly the same latency.
- At least eight displayed entries have exactly two distinct latency values, at least 75% use `10000ms`, and at least two remaining entries share the other value. This handles layouts mixing `10000ms` filler entries with a second fixed value such as `500ms`, without requiring every entry to match.

The extra space reserved for numeric ping is also removed. Detection is recalculated each frame, and the configured display returns as soon as neither condition matches. A single differing player, or three or more distinct latencies, keeps normal display. Empty and single-player lists are left unchanged. These rules are heuristics: genuinely identical or sufficiently clustered real pings can also be hidden.

## Build

The [Build workflow](../../actions/workflows/build.yml) runs on pushes, pull requests, and manual dispatches. It uses Java 25 to run `./gradlew build`, including the tests, and uploads the mod JAR with `SHA256SUMS.txt` as an Actions artifact.

## Supported Versions

Minecraft 26.3 (Fabric)

**Original Mod:** [Better Ping Display - Fabric](https://modrinth.com/mod/better-ping-display-fabric)

---

*Requires [Fabric Loader](https://fabricmc.net/) and Fabric API*
