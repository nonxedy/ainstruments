package com.antagon.ainstruments.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.antagon.ainstruments.Ainstruments;
import com.antagon.ainstruments.model.Instrument;

/**
 * Command handler for the /instrument command.
 */
public class InstrumentCommand implements CommandExecutor, TabCompleter {

    private final Ainstruments plugin;
    
    public InstrumentCommand(Ainstruments plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            sendHelp(player);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "help":
                sendHelp(player);
                break;
                
            case "list":
                listInstruments(player);
                break;
                
            case "get":
            case "give":
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "Usage: /instrument get <instrument>");
                    return true;
                }
                giveInstrument(player, args[1]);
                break;
                
            case "play":
                playInstrument(player);
                break;
                
            case "stop":
                stopInstrument(player);
                break;
                
            case "note":
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "Usage: /instrument note <note>");
                    return true;
                }
                tryPlayNote(player, args[1]);
                break;
                
            case "remove":
            case "clear":
                removeInstrument(player);
                break;
                
            default:
                player.sendMessage(ChatColor.RED + "Unknown subcommand. Use /instrument help for help.");
                break;
        }
        
        return true;
    }
    
    /**
     * Send help information to a player.
     * 
     * @param player The player
     */
    private void sendHelp(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== Instruments Help ===");
        player.sendMessage(ChatColor.YELLOW + "/instrument help" + ChatColor.WHITE + " - Show this help");
        player.sendMessage(ChatColor.YELLOW + "/instrument list" + ChatColor.WHITE + " - List available instruments");
        player.sendMessage(ChatColor.YELLOW + "/instrument get <instrument>" + ChatColor.WHITE + " - Get an instrument");
        player.sendMessage(ChatColor.YELLOW + "/instrument play" + ChatColor.WHITE + " - Start playing your instrument");
        player.sendMessage(ChatColor.YELLOW + "/instrument stop" + ChatColor.WHITE + " - Stop playing your instrument");
        player.sendMessage(ChatColor.YELLOW + "/instrument note <note>" + ChatColor.WHITE + " - Play a specific note (0-24)");
        player.sendMessage(ChatColor.YELLOW + "/instrument remove" + ChatColor.WHITE + " - Remove your instrument");
    }
    
    /**
     * List all available instruments to a player.
     * 
     * @param player The player
     */
    private void listInstruments(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== Available Instruments ===");
        player.sendMessage(ChatColor.YELLOW + "piano" + ChatColor.WHITE + " - A grand piano");
        player.sendMessage(ChatColor.YELLOW + "guitar" + ChatColor.WHITE + " - An acoustic guitar");
        player.sendMessage(ChatColor.YELLOW + "flute" + ChatColor.WHITE + " - A wooden flute");
    }
    
    /**
     * Give a player an instrument.
     * 
     * @param player The player
     * @param instrumentId The instrument ID
     */
    private void giveInstrument(Player player, String instrumentId) {
        if (plugin.getInstrumentManager().hasInstrument(player)) {
            player.sendMessage(ChatColor.RED + "You already have an instrument. Use /instrument remove first.");
            return;
        }
        
        boolean success = plugin.getInstrumentManager().givePlayerInstrument(player, instrumentId);
        
        if (success) {
            Instrument instrument = plugin.getInstrumentManager().getPlayerInstrument(player);
            player.sendMessage(ChatColor.GREEN + "You have received a " + instrument.getName() + "!");
            player.sendMessage(ChatColor.YELLOW + "Use /instrument play to start playing!");
        } else {
            player.sendMessage(ChatColor.RED + "Unknown instrument: " + instrumentId);
        }
    }
    
    /**
     * Start playing the player's instrument.
     * 
     * @param player The player
     */
    private void playInstrument(Player player) {
        if (!plugin.getInstrumentManager().hasInstrument(player)) {
            player.sendMessage(ChatColor.RED + "You don't have an instrument. Use /instrument get <instrument> first.");
            return;
        }
        
        Instrument instrument = plugin.getInstrumentManager().getPlayerInstrument(player);
        instrument.setPlaying(true);
        player.sendMessage(ChatColor.GREEN + "You started playing your " + instrument.getName() + ".");
        player.sendMessage(ChatColor.YELLOW + "Use /instrument note <0-24> to play notes!");
        player.sendMessage(ChatColor.YELLOW + "Or use /instrument stop to stop playing.");
    }
    
    /**
     * Stop playing the player's instrument.
     * 
     * @param player The player
     */
    private void stopInstrument(Player player) {
        if (!plugin.getInstrumentManager().hasInstrument(player)) {
            player.sendMessage(ChatColor.RED + "You don't have an instrument.");
            return;
        }
        
        Instrument instrument = plugin.getInstrumentManager().getPlayerInstrument(player);
        instrument.setPlaying(false);
        player.sendMessage(ChatColor.GREEN + "You stopped playing your " + instrument.getName() + ".");
    }
    
    /**
     * Play a specific note on the player's instrument.
     * 
     * @param player The player
     * @param noteStr The note as a string (0-24)
     */
    private void tryPlayNote(Player player, String noteStr) {
        if (!plugin.getInstrumentManager().hasInstrument(player)) {
            player.sendMessage(ChatColor.RED + "You don't have an instrument. Use /instrument get <instrument> first.");
            return;
        }
        
        Instrument instrument = plugin.getInstrumentManager().getPlayerInstrument(player);
        
        if (!instrument.isPlaying()) {
            player.sendMessage(ChatColor.RED + "Your instrument is not being played. Use /instrument play first.");
            return;
        }
        
        try {
            int note = Integer.parseInt(noteStr);
            if (note < 0 || note > 24) {
                player.sendMessage(ChatColor.RED + "Note must be between 0 and 24.");
                return;
            }
            
            instrument.playNote(note);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Invalid note format. Use a number between 0 and 24.");
        }
    }
    
    /**
     * Remove the player's instrument.
     * 
     * @param player The player
     */
    private void removeInstrument(Player player) {
        if (!plugin.getInstrumentManager().hasInstrument(player)) {
            player.sendMessage(ChatColor.RED + "You don't have an instrument.");
            return;
        }
        
        Instrument instrument = plugin.getInstrumentManager().getPlayerInstrument(player);
        String name = instrument.getName();
        
        // Stop playing if playing
        if (instrument.isPlaying()) {
            instrument.setPlaying(false);
        }
        
        plugin.getInstrumentManager().removePlayerInstrument(player);
        player.sendMessage(ChatColor.GREEN + "You put away your " + name + ".");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            List<String> subCommands = Arrays.asList("help", "list", "get", "play", "stop", "note", "remove");
            
            for (String subCommand : subCommands) {
                if (subCommand.startsWith(partial)) {
                    completions.add(subCommand);
                }
            }
        } else if (args.length == 2) {
            String subCommand = args[0].toLowerCase();
            String partial = args[1].toLowerCase();
            
            if (subCommand.equals("get") || subCommand.equals("give")) {
                // List available instruments
                List<String> instruments = Arrays.asList("piano", "guitar", "flute");
                
                for (String instrument : instruments) {
                    if (instrument.startsWith(partial)) {
                        completions.add(instrument);
                    }
                }
            } else if (subCommand.equals("note")) {
                // Suggest some common notes
                List<String> notes = Arrays.asList("0", "6", "12", "18", "24");
                
                for (String note : notes) {
                    if (note.startsWith(partial)) {
                        completions.add(note);
                    }
                }
            }
        }
        
        return completions;
    }
}
