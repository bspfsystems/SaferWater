# Using the SaferWater Bukkit Plugin

## Installation

To obtain a copy of SaferWater, please see the related section in [README.md](README.md). Simply drop the `.jar` file into the `plugins/` folder for your Bukkit installation, and then (re-)start the server.

The currently-supported versions of Bukkit are:
- 1.18.2

_Please Note: This plugin may work with other versions of Bukkit, but is not guaranteed to._

## Configuration

Please see [CONFIGURATION.md](CONFIGURATION.md) for information on configuring the plugin.

## In-Game Usage / Commands & Permissions

There is no main functionality that can be triggered or otherwise run by in-game commands. The plugin's functionality is in the backend spawning mechanics.

The only in-game command that can be run will reload the configuration file. The command, along with its respective description and permission node, is listed below:

**Base SaferWater Command:** The base command for all SaferWater commands. If this command has no arguments, a list of all subcommands that the sender has permission to use, and their respective syntax, will be displayed. **Please Note:** This permission **MUST** be granted to all that wish to use any SaferWater subcommand.
- `/saferwater` - `saferwater.command.saferwater`

**Reload Command:** Reloads the configuration file, adding and/or removing mobs from the water-spawning blacklist.
- `/saferwater reload` - `saferwater.command.saferwater.reload`
