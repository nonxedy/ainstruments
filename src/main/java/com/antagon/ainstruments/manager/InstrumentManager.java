package com.antagon.ainstruments.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.antagon.ainstruments.Ainstruments;
import com.antagon.ainstruments.model.Instrument;

/**
 * Manages all instruments in the plugin.
 */
public class InstrumentManager {
    
    private final Ainstruments plugin;
    private final Map<String, Class<? extends Instrument>> registeredInstruments;
    private final Map<UUID, Instrument> playerInstruments;
    
    public InstrumentManager(Ainstruments plugin) {
        this.plugin = plugin;
        this.registeredInstruments = new HashMap<>();
        this.playerInstruments = new HashMap<>();
        
        // Register default instruments
        registerDefaultInstruments();
    }
    
    /**
     * Register default instruments.
     */
    private void registerDefaultInstruments() {
        // Register all available instruments
        registerInstrument("piano", com.antagon.ainstruments.model.Piano.class);
        registerInstrument("guitar", com.antagon.ainstruments.model.Guitar.class);
        registerInstrument("flute", com.antagon.ainstruments.model.Flute.class);
        
        // More instruments can be registered here as they are implemented
        // registerInstrument("drums", Drums.class);
    }
    
    /**
     * Register a new instrument type.
     * 
     * @param id The unique identifier for this instrument
     * @param instrumentClass The instrument class
     */
    public void registerInstrument(String id, Class<? extends Instrument> instrumentClass) {
        registeredInstruments.put(id.toLowerCase(), instrumentClass);
    }
    
    /**
     * Get an instrument by its ID.
     * 
     * @param id The instrument ID
     * @return The instrument class, or null if not found
     */
    public Class<? extends Instrument> getInstrumentClass(String id) {
        return registeredInstruments.get(id.toLowerCase());
    }
    
    /**
     * Give a player an instrument.
     * 
     * @param player The player
     * @param instrumentId The instrument ID
     * @return true if successful, false if the instrument doesn't exist
     */
    public boolean givePlayerInstrument(Player player, String instrumentId) {
        Class<? extends Instrument> instrumentClass = getInstrumentClass(instrumentId);
        if (instrumentClass == null) {
            return false;
        }
        
        try {
            Instrument instrument = instrumentClass.getDeclaredConstructor(Player.class).newInstance(player);
            playerInstruments.put(player.getUniqueId(), instrument);
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to create instrument: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Remove an instrument from a player.
     * 
     * @param player The player
     */
    public void removePlayerInstrument(Player player) {
        playerInstruments.remove(player.getUniqueId());
    }
    
    /**
     * Check if a player currently has an instrument.
     * 
     * @param player The player
     * @return true if the player has an instrument, false otherwise
     */
    public boolean hasInstrument(Player player) {
        return playerInstruments.containsKey(player.getUniqueId());
    }
    
    /**
     * Get a player's current instrument.
     * 
     * @param player The player
     * @return The instrument, or null if they don't have one
     */
    public Instrument getPlayerInstrument(Player player) {
        return playerInstruments.get(player.getUniqueId());
    }
}
