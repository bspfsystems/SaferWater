# Configuring SaferWater for Bukkit

A default configuration file (`config.yml`) will be created in the plugin's data folder when you start the server for the first time after installing the plugin. You can then edit the configuration file as needed, and then run the reload command to reload the configuration file:
- `/saferwater reload`

When new releases of the plugin are made available, the default configuration file in the repository may be updated; however, the configuration file in the plugin's data folder will not be updated. While we try not to change the configuration file, sometimes it is unavoidable. You may obtain an up-to-date version of the default file from [here](https://bspfsystems.org/config-files/saferwater/bukkit/). You can simply drop the updated file in place of the old one, updating the values to reflect your requirements and/or previous settings. You can then run the reload command in-game to load the updated configuration.

The SaferWater plugin can accept an alternative name for its configuration file, if it is preferred to not use the default `config.yml` (the configuration file will be located in the plugin's data folder). The acceptable alternative name is `saferwater.yml`.

## Configuration Options

This section explains the Bukkit configuration options. The settings and their respective defaults are listed first in each section.

### General Plugin Settings

```
logging_level: "INFO"
disallowed_mobs:
```

- **logging_level:**
  - This is the logging level for the SaferWater plugin logger. It will *only* change the logging level for the plugin's logger.
  - NOTE: You may need to update `spigot.yml` to enable debugging for the trace and debug levels to display in the log files.
  - The available levels and their respective Log4j levels are:
    ```
    |============================|
    | Java Logger | Bukkit Log4j |
    | ------------|--------------|
    | SEVERE      | ERROR        |
    | WARNING     | WARN         |
    | INFO        | INFO         |
    | CONFIG      | DEBUG        |
    | FINE        | TRACE        |
    | FINER       | TRACE        |
    | FINEST      | TRACE        |
    |============================|
    ```
  - The default value is `"INFO"`.
    - A null value will use the default.
    - An empty value will use the default.
    - An invalid value will use the default.
- **disallowed_mobs:**
  - This should be a list of mobs (see the example below) that should not be allowed to spawn in the water.
    - This will include transformations (A Zombie transforming into a Drowned, if Drowned are on the list).
  - An example of the configuration can be seen below:
    ```
    disallowed_mobs:
      - "Drowned"
      - "Guardian"
    ```
  - The idea is to prevent the disallowed mobs from spawning anywhere in the column of water where they would be disallowed from spawning on or near the floor if the light level on the floor is high enough.
    - A null entry in the list will be skipped.
    - An empty entry in the list will be skipped.
    - An invalid entry in the list may cause a condition where no water spawns are denied.
