package com.antagon.ainstruments.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.antagon.ainstruments.Ainstruments;

/**
 * Listener for events related to voicechat and instruments.
 */
public class VoicechatEventListener implements Listener {
    
    private final Ainstruments plugin;
    
    public VoicechatEventListener(Ainstruments plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Handle player join - nothing to do yet
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Remove any instruments when player quits
        if (plugin.getInstrumentManager().hasInstrument(event.getPlayer())) {
            plugin.getInstrumentManager().removePlayerInstrument(event.getPlayer());
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // This will be used to handle instrument interaction
        // For example, right-clicking with an instrument item could open the sequencer UI
    }
    
    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        // This could be used for instrument hotkeys or switching between instruments
    }
}
