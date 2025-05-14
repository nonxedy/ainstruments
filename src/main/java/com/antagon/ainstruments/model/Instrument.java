package com.antagon.ainstruments.model;

import org.bukkit.entity.Player;

import com.antagon.ainstruments.Ainstruments;
import com.antagon.ainstruments.sequencer.NoteSequencer;

import de.maxhenkel.voicechat.api.VoicechatApi;

/**
 * Base class for all musical instruments.
 */
public abstract class Instrument {
    
    protected final Player player;
    protected final Ainstruments plugin;
    protected NoteSequencer sequencer;
    protected boolean isPlaying = false;
    
    /**
     * Create a new instrument for a player.
     * 
     * @param player The player who will use this instrument
     */
    public Instrument(Player player) {
        this.player = player;
        this.plugin = Ainstruments.getInstance();
        this.sequencer = new NoteSequencer(this);
    }
    
    /**
     * Get the player using this instrument.
     * 
     * @return The player
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Get the note sequencer for this instrument.
     * 
     * @return The note sequencer
     */
    public NoteSequencer getSequencer() {
        return sequencer;
    }
    
    /**
     * Check if the instrument is currently playing.
     * 
     * @return true if playing, false otherwise
     */
    public boolean isPlaying() {
        return isPlaying;
    }
    
    /**
     * Set whether the instrument is currently playing.
     * 
     * @param playing Whether the instrument is playing
     */
    public void setPlaying(boolean playing) {
        this.isPlaying = playing;
    }
    
    /**
     * Play a note with the given pitch on this instrument.
     * 
     * @param note The note to play (0-24)
     */
    public abstract void playNote(int note);
    
    /**
     * Play a chord (multiple notes at once) on this instrument.
     * 
     * @param notes Array of notes to play
     */
    public void playChord(int[] notes) {
        for (int note : notes) {
            playNote(note);
        }
    }
    
    /**
     * Get the instrument's display name.
     * 
     * @return The name
     */
    public abstract String getName();
    
    /**
     * Get the sound resource location for this instrument.
     * This is used to locate the sound files.
     * 
     * @return The sound resource location
     */
    public abstract String getSoundLocation();
    
    /**
     * Get the voice chat API.
     * 
     * @return The voice chat API
     */
    protected VoicechatApi getVoicechatApi() {
        // Get the voicechat API through our implementation
        return plugin.getVoicechatIntegration().getVoicechatApi();
    }
}
