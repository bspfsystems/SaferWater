# SaferWater

A plugin for Minecraft Bukkit servers that makes the underwater world a bit safer.

## Download

You can download the latest version of the plugin from [here](https://github.com/bspfsystems/SaferWater/releases/latest/).

The latest version is release 1.0.3.

## Build from Source

SaferWater uses [Apache Maven](https://maven.apache.org/) to build and handle dependencies.

### Requirements

- Java Development Kit (JDK) 8 or higher
- Git
- Apache Maven

### Compile / Build

Run the following commands to build the plugin:
```
git clone https://github.com/bspfsystems/SaferWater.git
cd SaferWater/
mvn clean install
```

The `.jar` file will be located in the `target/` folder for the Bukkit plugin.

## Installation

Simply drop the appropriate file into the `plugins/` folder for your Bukkit installation, and then (re-)start the server.

The currently-supported versions of Bukkit are:
- 1.18.1

_Please Note: This plugin may work with other versions of Bukkit, but is not guaranteed to._

### Configuration

A default configuration file (`config.yml`) will be created in the plugin's data folder when you start the server for the first time after installing the plugin. You can then edit the configuration file as needed, and then run the reload command to reload the configuration file:
- `/saferwater reload`

When new releases of the plugin are made available, the default configuration file may update; however, the configuration file in the plugin's data folder will not be updated. While we try not to change the configuration file, sometimes it is unavoidable. You may obtain an up-to-date version of the default file from [here](https://bspfsystems.org/config-files/saferwater/). You can simply drop the updated file in place of the old one, updating the values to reflect your requirements and/or previous settings. You can then run the reload command in-game to load the updated configuration.

The SaferWater plugin can accept alternative names for its configuration file, if the default `config.yml` is confusing to keep track of (configuration files will be in the plugin's data folder). The plugin will also accept `saferwater.yml` as a configuration file name. More information can be found at the top of the default configuration file (can be viewed [here](https://bspfsystems.org/config-files/saferwater/)).

## In-Game Usage / Commands & Permissions

There is no main functionality that can be triggered or otherwise run by in-game commands. The plugin's functionality is in the backend spawning mechanics.

The only in-game command that can be run will reload the configuration file. The command, along with its respective description and permission node, is listed below:

**Base SaferWater Command:** The base command for all SaferWater commands. If this command has no arguments, a list of all subcommands that the sender has permission to use, and their respective syntax, will be displayed. **Please Note:** This permission **MUST** be granted to all that wish to use any SaferWater subcommand.
- `/saferwater` - `saferwater.command.saferwater`

**Reload Command:** Reloads the configuration file, adding and/or removing mobs from the water-spawning blacklist.
- `/saferwater reload` - `saferwater.command.saferwater.reload`

## Contributing

### Pull Requests

Contributions to the project are welcome. SaferWater is a free and open source software project, created in the hopes that the community would find ways to improve it. If you make any improvements or other enhancements to SaferWater, we ask that you submit a Pull Request to merge the changes back upstream. We would enjoy the opportunity to give those improvements back to the wider community.

Various types of contributions are welcome, including (but not limited to):
- Security updates / patches
- Bug fixes
- Feature enhancements

We reserve the right to not include a contribution in the project if the contribution does not add anything substantive or otherwise reduces the functionality of SaferWater in a non-desirable way. That said, the idea of having free and open source software was that contributions would be accepted, and discussions over a potential contribution are welcome.

For licensing questions, please see the Licensing section.

### Project Layout

SaferWater somewhat follows the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html). This is not the definitive coding style of the project. Generally, it is best to try to copy the style of coding found in the class that you are editing.

## Support / Issues

Issues can be reported [here in GitHub](https://github.com/bspfsystems/SaferWater/issues/).

### First Steps

Before creating an issue, please search to see if anyone else has reported the same issue. Don't forget to search the closed issues. It is much easier for us (and will get you a faster response) to handle a single issue that affects multiple users than it is to have to deal with duplicates.

There is also a chance that your issue has been resolved previously. In this case, you can (ideally) find the answer to your problem without having to ask (new version of SaferWater, configuration update, etc).

### Creating an Issue

If no one has reported the issue previously, or the solution is not apparent, please open a new issue. When creating the issue, please give it a descriptive title (no "It's not working", please), and put as much detail into the description as possible. The more details you add, the easier it becomes for us to solve the issue. Helpful items may include:
- A descriptive title for the issue
- The version of SaferWater you are using
- The version of Minecraft you are using
- The Bukkit implementation you are using (CraftBukkit / Spigot / Paper / etc.)
- Logs and/or stack traces
- Any steps to reproducing the issue
- Anything else that might be helpful in solving your issue.

_Note:_ Please redact any Personally-Identifiable Information (PII) when you create your issue. These may appear in logs or stack traces. Examples include (but are not limited to):
- Real names of players / server administrators
- Usernames of accounts on computers (may appear in logs or stack traces)
- IP addresses / hostnames
- etc.

If you are not sure, you can always redact or otherwise change the data.

### Non-Acceptable Issues

Issues such as "I need help" or "It doesn't work" will not be addressed and/or will be closed with no assistance given. These type of issues do not have any meaningful details to properly address the problem. Other issues that will not be addressed and/or closed without help include (but are not limited to):
- How to install SaferWater (explained in README)
- How to configure SaferWater (explained in README and default configuration)
- How to create plugins
- How to set up a development environment
- How to install plugins
- How to create a server
- Other issues of similar nature...

This is not a help forum for server administration or non-project-related coding issues. Other resources, such as [Google](https://www.google.com/), should have answers to most questions not related to SaferWater.

## Licensing

The project uses the following licenses:
- [The GNU General Public License, Version 3](https://www.gnu.org/licenses/gpl-3.0.en.html)

### Contributions & Licensing

Contributions to the project will remain licensed under the GPLv3 license, as defined by this particular license. Copyright/ownership of the contributions shall be governed by the license. The use of an open source license in the hopes that contributions to the project will have better clarity on legal rights of those contributions.

_Please Note: This is not legal advice. If you are unsure on what your rights are, please consult a lawyer._
