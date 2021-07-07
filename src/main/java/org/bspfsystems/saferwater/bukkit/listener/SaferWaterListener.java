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

package org.bspfsystems.saferwater.bukkit.listener;

import org.bspfsystems.saferwater.bukkit.SaferWaterPlugin;
import org.bspfsystems.saferwater.bukkit.command.SaferWaterTabExecutor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.command.Command;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link Listener} for all SaferWater {@link Event}s.
 */
public final class SaferWaterListener implements Listener {
    
    private final SaferWaterPlugin saferWaterPlugin;
    
    /**
     * Constructs a new SaferWater {@link Listener}.
     * 
     * @param saferWaterPlugin The {@link SaferWaterPlugin}.
     */
    public SaferWaterListener(@NotNull final SaferWaterPlugin saferWaterPlugin) {
        this.saferWaterPlugin = saferWaterPlugin;
    }
    
    /**
     * Manages {@link Creature} spawning to determine if a spawn in water should
     * be allowed.
     * 
     * @param event The {@link CreatureSpawnEvent}.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCreatureSpawn(final CreatureSpawnEvent event) {
        
        final LivingEntity livingEntity = event.getEntity();
        if (!livingEntity.isInWater()) {
            return;
        }
        
        if (!(livingEntity instanceof Creature)) {
            return;
        }
        
        final Creature creature = (Creature) livingEntity;
        if (!this.saferWaterPlugin.isWaterSpawnDisabled(creature)) {
            return;
        }
        
        final Location spawnLocation = creature.getLocation();
        final World spawnWorld = spawnLocation.getWorld();
        if (spawnWorld == null) {
            return;
        }
        
        Block lastWaterBlock = spawnWorld.getBlockAt(spawnLocation);
        Block checkBlock = lastWaterBlock.getRelative(BlockFace.DOWN, 1);
        
        while (!this.checkBlock(checkBlock)) {
            if (checkBlock.getType() == Material.WATER) {
                lastWaterBlock = checkBlock;
            }
            checkBlock = checkBlock.getRelative(BlockFace.DOWN, 1);
        }
        
        if (lastWaterBlock.getLightLevel() < (byte) 8) {
            event.setCancelled(true);
        }
    }
    
    /**
     * Checks the given {@link Block} and the 2 {@link Block}s below to see if
     * the bottom of the body of water has been reached, and thus the light
     * level (from blocks) should be checked.
     * 
     * @param block The {@link Block} to check.
     * @return {@code true} if more {@link Block}s below this one need to be
     *         checked, {@code false} if the light level can be checked at this
     *         {@link Block}.
     */
    private boolean checkBlock(@NotNull final Block block) {
        
        if (this.checkWater(block.getType(), block.getBlockData())) {
            return true;
        }
        
        final Block belowBlock = block.getRelative(BlockFace.DOWN, 1);
        if (this.checkWater(belowBlock.getType(), belowBlock.getBlockData())) {
            return true;
        }
        
        final Block belowBelowBlock = block.getRelative(BlockFace.DOWN, 2);
        return this.checkWater(belowBelowBlock.getType(), belowBelowBlock.getBlockData());
    }
    
    /**
     * Checks to see if the given {@link Material} is {@link Material#WATER} or
     * one of the coral block variants
     * if the given {@link BlockData} is {@link Waterlogged} and is currently
     * waterlogged {@link Waterlogged#isWaterlogged()}.
     * 
     * @param material The {@link Material} to check.
     * @param blockData The {@link BlockData} to check.
     * @return {@code true} if the {@link Material} is {@link Material#WATER} or
     *         if the {@link BlockData} is {@link Waterlogged} and currently is
     *         waterlogged, {@code false} otherwise.
     */
    private boolean checkWater(@NotNull final Material material, @NotNull final BlockData blockData) {
        
        switch (material) {
            case WATER:
            case DEAD_TUBE_CORAL_BLOCK:
            case DEAD_BRAIN_CORAL_BLOCK:
            case DEAD_BUBBLE_CORAL_BLOCK:
            case DEAD_FIRE_CORAL_BLOCK:
            case DEAD_HORN_CORAL_BLOCK:
            case TUBE_CORAL_BLOCK:
            case BRAIN_CORAL_BLOCK:
            case BUBBLE_CORAL_BLOCK:
            case FIRE_CORAL_BLOCK:
            case HORN_CORAL_BLOCK:
                return true;
            default:
                break;
        }
        
        return blockData instanceof Waterlogged && ((Waterlogged) blockData).isWaterlogged();
    }
    
    /**
     * Deals with tab-completion for the base {@code /saferwater}
     * {@link Command}.
     * <p>
     * All subcommand tab-completion is handled in the
     * {@link SaferWaterTabExecutor}.
     * 
     * @param event The {@link PlayerCommandSendEvent}.
     */
    @EventHandler
    public void onPlayerCommandSend(final PlayerCommandSendEvent event) {
        event.getCommands().removeAll(this.saferWaterPlugin.playerCommandSend(event.getPlayer()));
    }
}
