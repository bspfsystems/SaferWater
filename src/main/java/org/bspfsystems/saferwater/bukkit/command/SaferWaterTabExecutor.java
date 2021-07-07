/*
 * This file is part of the SaferWater plugin for
 * Bukkit servers for Minecraft.
 *
 * Copyright (C) 2021 BSPF Systems, LLC (https://bspfsystems.org/)
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

package org.bspfsystems.saferwater.bukkit.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bspfsystems.saferwater.bukkit.SaferWaterPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the command executor and tab-completer implementation for the
 * {@code /saferwater} command and all subcommands.
 */
public final class SaferWaterTabExecutor implements TabExecutor {
    
    private final SaferWaterPlugin saferWaterPlugin;
    
    /**
     * Constructs a new {@code /saferwater} command.
     * 
     * @param saferWaterPlugin The {@link SaferWaterPlugin}.
     */
    public SaferWaterTabExecutor(@NotNull final SaferWaterPlugin saferWaterPlugin) {
        this.saferWaterPlugin = saferWaterPlugin;
    }
    
    /**
     * Performs the execution of the {@code /saferwater} command and all
     * subcommands.
     * 
     * @param sender The {@link CommandSender} executing the {@link Command}.
     * @param command The {@link Command} being executed.
     * @param label The label (alias) of the {@link Command} that was actually
     *              used.
     * @param args Any command-line arguments.
     * @return {@code true} if the execution of the {@link Command} falls within
     *         the intended usage, {@code false} otherwise.
     */
    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        
        if (args.length == 0) {
            return this.sendSubCommands(sender, command);
        }
        
        final ArrayList<String> argList = new ArrayList<String>(Arrays.asList(args));
        final String subCommand = argList.remove(0);
        
        if (subCommand.equalsIgnoreCase("reload")) {
            
            if (!sender.hasPermission("saferwater.command.saferwater.reload")) {
                sender.sendMessage(this.getPermissionMessage(command));
                return true;
            }
            if (!argList.isEmpty()) {
                sender.sendMessage("§r§cSyntax: /saferwater reload§r");
                return true;
            }
            
            sender.sendMessage("§r§6Reloading the SaferWater configuration. Please check your spawns in a few seconds to verify that it has reloaded properly.§r");
            this.saferWaterPlugin.reloadConfig(sender);
            return true;
            
        } else {
            return this.sendSubCommands(sender, command);
        }
    }
    
    /**
     * Performs the tab-completion of the {@code /saferwater} command and all
     * subcommands.
     *
     * @param sender The {@link CommandSender} tab-completing for the the
     *               {@link Command}.
     * @param command The {@link Command} being tab-completed.
     * @param label The label (alias) of the {@link Command} that was actually
     *              used.
     * @param args Any command-line arguments.
     * @return The {@link List} of all possible tab-completions.
     */
    @NotNull
    @Override
    public List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        
        final ArrayList<String> argList = new ArrayList<String>(Arrays.asList(args));
        final ArrayList<String> completions = new ArrayList<String>();
        
        if (sender.hasPermission("saferwater.command.saferwater.reload")) {
            completions.add("reload");
        }
        
        if (argList.isEmpty()) {
            return completions;
        }
        
        final String subCommand = argList.remove(0);
        if (argList.isEmpty()) {
            completions.removeIf(completion -> !completion.toLowerCase().startsWith(subCommand.toLowerCase()));
            return completions;
        }
        
        completions.clear();
        return completions;
    }
    
    /**
     * Sends the list of subcommands that the {@link CommandSender} has permission to use.
     * 
     * @param sender The {@link CommandSender}.
     * @param command The {@link Command}.
     * @return {@code true} if the execution of the {@link Command} falls within
     *         the intended usage, {@code false} otherwise.
     */
    private boolean sendSubCommands(@NotNull final CommandSender sender, @NotNull final Command command) {
        
        final boolean permissionRelog = sender.hasPermission("saferwater.command.saferwater.reload");
        
        if (!permissionRelog) {
            sender.sendMessage(this.getPermissionMessage(command));
            return true;
        }
        
        sender.sendMessage("§r§6Available commands:§r");
        sender.sendMessage("§r§8----------------------------------------------------------------§8");
        
        if (permissionRelog) {
            sender.sendMessage("§r §f-§r §b/saferwater reload§r");
        }
        
        return true;
    }
    
    /**
     * Gets the permission message for the {@link Command}, or a default one if
     * none exists.
     * 
     * @param command The {@link Command} to get the permission message of.
     * @return The permission message to send to the {@link CommandSender}.
     */
    @NotNull
    private String getPermissionMessage(@NotNull final Command command) {
        return command.getPermissionMessage() != null ? command.getPermissionMessage() : "§r§cYou do not have permission to execute this command.§r";
    }
}
