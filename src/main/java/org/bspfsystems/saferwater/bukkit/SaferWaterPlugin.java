/*
 * This file is part of the SaferWater plugin for
 * Bukkit servers for Minecraft.
 *
 * Copyright (C) 2021-2022 BSPF Systems, LLC (https://bspfsystems.org/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.bspfsystems.saferwater.bukkit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bspfsystems.saferwater.bukkit.command.SaferWaterTabExecutor;
import org.bspfsystems.saferwater.bukkit.listener.SaferWaterListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

/**
 * This class is the main entrypoint into Bukkit's framework for the plugin's
 * event handling.
 */
public final class SaferWaterPlugin extends JavaPlugin {
    
    private Logger logger;
    
    private HashSet<Class<? extends Creature>> waterSpawnDisabled;
    
    /**
     * Explicitly define the default constructor.
     */
    public SaferWaterPlugin() {
        super();
    }
    
    /**
     * Enables the plugin, displaying licensing information as well as
     * registering the event listener(s) for spawning information.
     */
    @Override
    public void onEnable() {
        
        this.logger = this.getLogger();
    
        this.logger.log(Level.INFO, "///////////////////////////////////////////////////////////////////////////");
        this.logger.log(Level.INFO, "//                                                                       //");
        this.logger.log(Level.INFO, "// SaferWater plugin for Bukkit servers for Minecraft.                   //");
        this.logger.log(Level.INFO, "// Copyright (C) 2021-2022 BSPF Systems, LLC (https://bspfsystems.org/)  //");
        this.logger.log(Level.INFO, "//                                                                       //");
        this.logger.log(Level.INFO, "// This program is free software: you can redistribute it and/or modify  //");
        this.logger.log(Level.INFO, "// it under the terms of the GNU General Public License as published by  //");
        this.logger.log(Level.INFO, "// the Free Software Foundation, either version 3 of the License, or     //");
        this.logger.log(Level.INFO, "// (at your option) any later version.                                   //");
        this.logger.log(Level.INFO, "//                                                                       //");
        this.logger.log(Level.INFO, "// This program is distributed in the hope that it will be useful,       //");
        this.logger.log(Level.INFO, "// but WITHOUT ANY WARRANTY; without even the implied warranty of        //");
        this.logger.log(Level.INFO, "// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         //");
        this.logger.log(Level.INFO, "// GNU General Public License for more details.                          //");
        this.logger.log(Level.INFO, "//                                                                       //");
        this.logger.log(Level.INFO, "// You should have received a copy of the GNU General Public License     //");
        this.logger.log(Level.INFO, "// along with this program.  If not, see <http://www.gnu.org/licenses/>. //");
        this.logger.log(Level.INFO, "//                                                                       //");
        this.logger.log(Level.INFO, "///////////////////////////////////////////////////////////////////////////");
        
        this.waterSpawnDisabled = new HashSet<Class<? extends Creature>>();
        
        this.getServer().getPluginManager().registerEvents(new SaferWaterListener(this), this);
        
        final TabExecutor saferWaterTabExecutor = new SaferWaterTabExecutor(this);
        this.registerCommand("saferwater", saferWaterTabExecutor);
        
        this.reloadConfig(this.getServer().getConsoleSender(), false);
    }
    
    /**
     * Registers the {@link PluginCommand} with the given name to the given
     * {@link TabExecutor}.
     * <p>
     * If no {@link PluginCommand} is found, an error will be logged an a
     * {@link RuntimeException} will be thrown.
     * 
     * @param commandName The name of the {@link PluginCommand} to retrieve.
     * @param tabExecutor The {@link TabExecutor} to register to the
     *                    {@link PluginCommand}.
     * @throws RuntimeException If no {@link PluginCommand} with the given name
     *                          can be found.
     */
    private void registerCommand(@NotNull final String commandName, @NotNull final TabExecutor tabExecutor) throws RuntimeException {
        final PluginCommand command = this.getCommand(commandName);
        if (command == null) {
            this.logger.log(Level.SEVERE, "Cannot find the /" + commandName + " command.");
            this.logger.log(Level.SEVERE, "SaferWater will not deny any water spawns.");
            throw new RuntimeException("Cannot find the /" + commandName + " command.");
        }
        command.setExecutor(tabExecutor);
        command.setTabCompleter(tabExecutor);
    }
    
    /**
     * Checks to see if the specified {@link Creature} is one of the ones that
     * is not allowed to spawn in certain water conditions.
     * 
     * @param creature The {@link Creature} to check.
     * @return {@code true} If the {@link Creature} is disallowed from spawning
     *         under certain conditions, {@code false} otherwise.
     */
    public boolean isWaterSpawnDisabled(@NotNull final Creature creature) {
        for (final Class<? extends Creature> clazz : this.waterSpawnDisabled) {
            if (clazz.isInstance(creature)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Removes any unnecessary tab-completions for all base {@link Command}s
     * that are registered with this {@link Plugin}.
     * <p>
     * All subcommand/argument tab-completions are handled in the respective
     * {@link TabExecutor}.
     * 
     * @param player The {@link Player} performing the tab-completion.
     * @return The tab-completions to remove.
     */
    @NotNull
    public Collection<String> onPlayerCommandSend(@NotNull final Player player) {
        
        final Collection<String> removals = new HashSet<String>();
        for (final String commandName : this.getDescription().getCommands().keySet()) {
            
            removals.add(this.getName().toLowerCase() + ":" + commandName);
            final PluginCommand command = this.getServer().getPluginCommand(commandName);
            if (command != null && !command.testPermissionSilent(player)) {
                removals.add(commandName);
            }
        }
        
        return removals;
    }
    
    /**
     * Reloads the configuration file, displaying any error messages to the
     * given {@link CommandSender}.
     * 
     * @param sender The {@link CommandSender} that triggered the configuration
     *               reload.
     */
    public void reloadConfig(@NotNull final CommandSender sender) {
        this.reloadConfig(sender, true);
    }
    
    /**
     * Reloads the configuration file, displaying any error messages to the
     * given {@link CommandSender} if this was triggered via {@link Command}.
     * 
     * @param sender The {@link CommandSender} that triggered the configuration
     *               reload.
     * @param command If {@code true}, error messages will be displayed to the
     *                {@link CommandSender} if any error occurs. If
     *                {@code false}, errors will only be shown in the console
     *                logs.
     */
    private void reloadConfig(@NotNull final CommandSender sender, final boolean command) {
        
        this.waterSpawnDisabled.clear();
        final BukkitScheduler scheduler = this.getServer().getScheduler();
        scheduler.runTaskAsynchronously(this, () -> {
            
            final File configDirectory = this.getDataFolder();
            try {
                if (configDirectory.exists()) {
                    if (!configDirectory.isDirectory()) {
                        this.logger.log(Level.WARNING, "SaferWater configuration directory is not a directory: " + configDirectory.getPath());
                        this.logger.log(Level.WARNING, "SaferWater will not deny any water spawns.");
                        if (command) {
                            sender.sendMessage("§r§cAn error has occurred while reloading the SaferWater configuration. Please try again. If the error persists, please report it to a server administrator.§r");
                        }
                        return;
                    }
                } else if (!configDirectory.mkdirs()) {
                    this.logger.log(Level.WARNING, "SaferWater configuration directory not created at " + configDirectory.getPath());
                    this.logger.log(Level.WARNING, "SaferWater will not deny any water spawns.");
                    if (command) {
                        sender.sendMessage("§r§cAn error has occurred while reloading the SaferWater configuration. Please try again. If the error persists, please report it to a server administrator.§r");
                    }
                    return;
                }
            } catch (SecurityException e) {
                this.logger.log(Level.WARNING, "Unable to validate the SaferWater configuration directory at " + configDirectory.getPath());
                this.logger.log(Level.WARNING, "SaferWater will not deny any water spawns.");
                if (command) {
                    sender.sendMessage("§r§cAn error has occurred while reloading the SaferWater configuration. Please try again. If the error persists, please report it to a server administrator.§r");
                }
                return;
            }
            
            File configFile = new File(configDirectory, "saferwater.yml");
            try {
                
                if (!configFile.exists() || !configFile.isFile()) {
                    configFile = new File(configDirectory, "config.yml");
                }
                
                if (configFile.exists()) {
                    if (!configFile.isFile()) {
                        this.logger.log(Level.WARNING, "SaferWater configuration file is not a file: " + configFile.getPath());
                        this.logger.log(Level.WARNING, "SaferWater will not deny any water spawns.");
                        if (command) {
                            sender.sendMessage("§r§cAn error has occurred while reloading the SaferWater configuration. Please try again. If the error persists, please report it to a server administrator.§r");
                        }
                        return;
                    }
                } else {
                    if (!configFile.createNewFile()) {
                        this.logger.log(Level.WARNING, "SaferWater configuration file not created at " + configFile.getPath());
                        this.logger.log(Level.WARNING, "SaferWater will not deny any water spawns.");
                        if (command) {
                            sender.sendMessage("§r§cAn error has occurred while reloading the SaferWater configuration. Please try again. If the error persists, please report it to a server administrator.§r");
                        }
                        return;
                    }
                    
                    final InputStream defaultConfig = this.getResource(configFile.getName());
                    final FileOutputStream outputStream = new FileOutputStream(configFile);
                    final byte[] buffer = new byte[4096];
                    int bytesRead;
                    
                    if (defaultConfig == null) {
                        this.logger.log(Level.WARNING, "SaferWater default configuration file not found. Possible compilation/build issue with the plugin.");
                        this.logger.log(Level.WARNING, "SaferWater will not deny any water spawns.");
                        if (command) {
                            sender.sendMessage("§r§cAn error has occurred while reloading the SaferWater configuration. Please try again. If the error persists, please report it to a server administrator.§r");
                        }
                        return;
                    }
                    
                    while ((bytesRead = defaultConfig.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    
                    outputStream.flush();
                    defaultConfig.close();
                    
                    this.logger.log(Level.WARNING, "SaferWater configuration file did not exist at " + configFile.getPath());
                    this.logger.log(Level.WARNING, "SaferWater will not deny any water spawns (other than any defaults in the default configuration file).");
                    this.logger.log(Level.WARNING, "Please update the configuration as required for your installation, and then run \"/saferwater reload\".");
                    if (command) {
                        sender.sendMessage("§r§cThe SaferWater configuration file did not exist; a copy of the default has been made and placed in the correct location.§r");
                        sender.sendMessage("§r§cPlease update the configuration as required for the installation, and then run§r §b/saferwater reload§r§c.§r");
                    }
                }
            } catch (SecurityException | IOException e) {
                this.logger.log(Level.WARNING, "Unable to load the SaferWater configuration file at " + configFile.getPath());
                this.logger.log(Level.WARNING, "SaferWater will not deny any water spawns.");
                this.logger.log(Level.WARNING, e.getClass().getSimpleName() + " thrown.", e);
                if (command) {
                    sender.sendMessage("§r§cAn error has occurred while reloading the SaferWater configuration. Please try again. If the error persists, please report it to a server administrator.§r");
                }
                return;
            }
            
            final YamlConfiguration config = new YamlConfiguration();
            try {
                config.load(configFile);
            } catch (IOException | InvalidConfigurationException | IllegalArgumentException e) {
                this.logger.log(Level.WARNING, "Unable to load the SaferWater configuration.");
                this.logger.log(Level.WARNING, "SaferWater will not deny any water spawns.");
                this.logger.log(Level.WARNING, e.getClass().getSimpleName() + " thrown.", e);
                if (command) {
                    sender.sendMessage("§r§cAn error has occurred while reloading the SaferWater configuration. Please try again. If the error persists, please report it to a server administrator.§r");
                }
                return;
            }
            
            final Level loggingLevel;
            Level rawLoggingLevel;
            try {
                rawLoggingLevel = Level.parse(config.getString("logging_level", "INFO"));
            } catch (NullPointerException | IllegalArgumentException e) {
                this.logger.log(Level.WARNING, "Unable to load the SaferWater logging level.");
                this.logger.log(Level.WARNING, "Will use the default level (INFO).");
                this.logger.log(Level.WARNING, e.getClass().getSimpleName() + " thrown.", e);
                if (command) {
                    sender.sendMessage("§r§cAn error has occurred while reloading the SaferWater configuration. Please try again. If the error persists, please report it to a server administrator.§r");
                }
                rawLoggingLevel = Level.INFO;
            }
            loggingLevel = rawLoggingLevel;
            
            final List<String> disallowedMobsRaw = config.getStringList("disallowed_mobs");
            final List<Class<? extends Creature>> disallowedMobs = new ArrayList<Class<? extends Creature>>();
            for (final String mob : disallowedMobsRaw) {
                try {
                    final Class<?> clazz = Class.forName("org.bukkit.entity." + mob);
                    if (!Creature.class.isAssignableFrom(clazz)) {
                        this.logger.log(Level.WARNING, "Class " + clazz.getName() + " is not of type " + Creature.class.getName() + ", cannot add to denied spawns list.");
                        continue;
                    }
                    disallowedMobs.add(clazz.asSubclass(Creature.class));
                } catch (LinkageError | ClassNotFoundException | NullPointerException | ClassCastException e) {
                    this.logger.log(Level.WARNING, "Unable to convert " + mob + " to a Class in org.bukkit.entity.");
                    this.logger.log(Level.WARNING, "Will not use " + mob + " as a disallowed mob.");
                    this.logger.log(Level.WARNING, e.getClass().getSimpleName() + " thrown.", e);
                    return;
                }
            }
            
            scheduler.runTask(this, () -> {
                
                this.logger.setLevel(loggingLevel);
                this.waterSpawnDisabled.addAll(disallowedMobs);
                if (command) {
                    sender.sendMessage("§r§aThe SaferWater configuration has been reloaded. Please verify your mob spawns with the configuration file.§r");
                }
            });
        });
    }
}
