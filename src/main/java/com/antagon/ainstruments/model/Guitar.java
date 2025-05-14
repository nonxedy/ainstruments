package com.antagon.ainstruments.model;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import de.maxhenkel.voicechat.api.VoicechatApi;

/**
 * Guitar instrument implementation.
 */
public class Guitar extends Instrument {

    private static final String SOUND_BASE_NAME = "guitar";
    
    /**
     * Create a new guitar for a player.
     * 
     * @param player The player who will use this guitar
     */
    public Guitar(Player player) {
        super(player);
    }
    
    @Override
    public void playNote(int note) {
        if (!isPlaying()) {
            return;
        }
        
        // Ensure the note is in valid range (0-24)
        note = Math.max(0, Math.min(24, note));
        
        // With VoiceChat API
        if (plugin.getVoicechatIntegration().isVoicechatEnabled()) {
            playNoteWithVoiceChat(note);
        } else {
            // Fallback to normal Minecraft sounds
            playNoteWithMinecraftSound(note);
        }
    }
    
    /**
     * Play a note using the VoiceChat API.
     * 
     * @param note The note to play
     */
    private void playNoteWithVoiceChat(int note) {
        VoicechatApi api = plugin.getVoicechatIntegration().getVoicechatApi();
        if (api == null) {
            return;
        }
        
        try {
            // Create sound filename based on the note
            String soundFileName = SOUND_BASE_NAME + "_" + note + ".ogg";
            
            // This is a placeholder for the actual VoiceChat API usage
            plugin.getLogger().info("Playing note: " + note + " on guitar for player " + player.getName() + " with VoiceChat API");
        } catch (Exception e) {
            plugin.getLogger().severe("Error playing note with VoiceChat API: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Play a note using Minecraft's built-in sound system as a fallback.
     * 
     * @param note The note to play
     */
    private void playNoteWithMinecraftSound(int note) {
        try {
            // Map the note to a Minecraft note block note (0-24) to a Minecraft note (0-2)
            float pitch = mapNoteToPitch(note);
            
            // Play sound to the player and nearby players
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 1.0f, pitch);
            plugin.getLogger().info("Playing note: " + note + " (pitch: " + pitch + ") on guitar for player " + player.getName());
        } catch (Exception e) {
            plugin.getLogger().severe("Error playing note with Minecraft sound: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Map a note value (0-24) to a Minecraft pitch value.
     * 
     * @param note The note value (0-24)
     * @return The pitch value (0.5-2.0)
     */
    private float mapNoteToPitch(int note) {
        // Map 0-24 to 0.5-2.0
        return 0.5f + ((float)note / 24.0f) * 1.5f;
    }
    
    @Override
    public String getName() {
        return "Guitar";
    }
    
    @Override
    public String getSoundLocation() {
        return "ainstruments:sounds/" + SOUND_BASE_NAME;
    }
}
