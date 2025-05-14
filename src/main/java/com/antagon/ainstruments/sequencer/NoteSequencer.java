package com.antagon.ainstruments.sequencer;

import org.bukkit.scheduler.BukkitTask;

import com.antagon.ainstruments.Ainstruments;
import com.antagon.ainstruments.model.Instrument;

/**
 * Handles sequencing notes for instruments.
 * This allows players to create sequences of notes that can be played back.
 */
public class NoteSequencer {
    
    private final Instrument instrument;
    private final Ainstruments plugin;
    
    // Sequencer grid - notes[row][column]
    // rows represent pitch (0 = highest, size-1 = lowest)
    // columns represent time (0 = start, size-1 = end)
    private boolean[][] notes;
    
    // Sequencer properties
    private int width = 16; // Number of time steps
    private int height = 25; // Number of notes (pitches)
    private int currentColumn = 0;
    private BukkitTask playbackTask;
    private int tempo = 120; // BPM
    
    /**
     * Create a new note sequencer for an instrument.
     * 
     * @param instrument The instrument this sequencer controls
     */
    public NoteSequencer(Instrument instrument) {
        this.instrument = instrument;
        this.plugin = Ainstruments.getInstance();
        this.notes = new boolean[height][width];
    }
    
    /**
     * Toggle a note in the sequencer grid.
     * 
     * @param row The row (pitch)
     * @param column The column (time step)
     * @return The new state (true = on, false = off)
     */
    public boolean toggleNote(int row, int column) {
        if (row < 0 || row >= height || column < 0 || column >= width) {
            return false;
        }
        
        notes[row][column] = !notes[row][column];
        return notes[row][column];
    }
    
    /**
     * Set a note in the sequencer grid.
     * 
     * @param row The row (pitch)
     * @param column The column (time step)
     * @param state The state (true = on, false = off)
     */
    public void setNote(int row, int column, boolean state) {
        if (row < 0 || row >= height || column < 0 || column >= width) {
            return;
        }
        
        notes[row][column] = state;
    }
    
    /**
     * Get the state of a note in the sequencer grid.
     * 
     * @param row The row (pitch)
     * @param column The column (time step)
     * @return The state (true = on, false = off)
     */
    public boolean getNote(int row, int column) {
        if (row < 0 || row >= height || column < 0 || column >= width) {
            return false;
        }
        
        return notes[row][column];
    }
    
    /**
     * Clear the entire sequencer grid.
     */
    public void clearAll() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                notes[i][j] = false;
            }
        }
    }
    
    /**
     * Start playing the sequencer.
     */
    public void play() {
        if (playbackTask != null) {
            return;
        }
        
        currentColumn = 0;
        instrument.setPlaying(true);
        
        // Calculate the delay between columns in ticks (1 tick = 50ms)
        // 60000 ms / BPM = ms per beat
        // 60000 / (tempo * width/4) = ms per column (assuming 4/4 time)
        long delayTicks = Math.max(1, Math.round(60000.0 / (tempo * width/4.0) / 50.0));
        
        playbackTask = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            playColumn(currentColumn);
            
            currentColumn++;
            if (currentColumn >= width) {
                currentColumn = 0;
            }
        }, 0, delayTicks);
    }
    
    /**
     * Stop playing the sequencer.
     */
    public void stop() {
        if (playbackTask != null) {
            playbackTask.cancel();
            playbackTask = null;
        }
        
        instrument.setPlaying(false);
    }
    
    /**
     * Play all notes in a specific column.
     * 
     * @param column The column to play
     */
    private void playColumn(int column) {
        // Collect all active notes in this column
        for (int row = 0; row < height; row++) {
            if (notes[row][column]) {
                instrument.playNote(row);
            }
        }
    }
    
    /**
     * Get the width of the sequencer grid (time steps).
     * 
     * @return The width
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Set the width of the sequencer grid.
     * 
     * @param width The new width
     */
    public void setWidth(int width) {
        if (width < 1) {
            return;
        }
        
        boolean[][] newNotes = new boolean[height][width];
        
        // Copy existing notes where possible
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < Math.min(width, this.width); j++) {
                newNotes[i][j] = notes[i][j];
            }
        }
        
        this.width = width;
        this.notes = newNotes;
    }
    
    /**
     * Get the height of the sequencer grid (pitches).
     * 
     * @return The height
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Set the tempo in beats per minute.
     * 
     * @param tempo The new tempo
     */
    public void setTempo(int tempo) {
        this.tempo = Math.max(20, Math.min(300, tempo));
        
        // Restart playback if currently playing
        if (playbackTask != null) {
            stop();
            play();
        }
    }
    
    /**
     * Get the tempo in beats per minute.
     * 
     * @return The tempo
     */
    public int getTempo() {
        return tempo;
    }
}
