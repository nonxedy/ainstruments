package com.antagon.ainstruments;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.antagon.ainstruments.command.InstrumentCommand;
import com.antagon.ainstruments.listener.VoicechatEventListener;
import com.antagon.ainstruments.manager.InstrumentManager;

import de.maxhenkel.voicechat.api.BukkitVoicechatService;

/**
 * Main class for the Ainstruments plugin.
 * This plugin allows players to play musical instruments using the Simple Voice Chat API.
 */
public class Ainstruments extends JavaPlugin {
    private static final Logger LOGGER = Logger.getLogger("ainstruments");
    private static Ainstruments instance;
    private InstrumentManager instrumentManager;
    private VoicechatIntegration voicechatIntegration;

    @Override
    public void onEnable() {
        instance = this;
        LOGGER.info("ainstruments enabled");

        // Initialize VoiceChat integration
        voicechatIntegration = new VoicechatIntegration();

        // Register with VoiceChat API
        BukkitVoicechatService service = getServer().getServicesManager().load(BukkitVoicechatService.class);
        if (service != null) {
            service.registerPlugin(voicechatIntegration);
            LOGGER.info("Successfully registered with VoiceChat API");
        } else {
            LOGGER.warning("Failed to register with VoiceChat API - VoiceChat plugin might not be loaded yet");
        }

        // Initialize managers and listeners
        initializeManagers();
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        LOGGER.info("ainstruments disabled");
    }

    private void initializeManagers() {
        // Initialize instrument manager
        instrumentManager = new InstrumentManager(this);
    }

    private void registerListeners() {
        // Register event listeners
        Bukkit.getPluginManager().registerEvents(new VoicechatEventListener(this), this);
    }

    private void registerCommands() {
        // Register the instrument command
        getCommand("instrument").setExecutor(new InstrumentCommand(this));
        getCommand("instrument").setTabCompleter(new InstrumentCommand(this));
    }

    /**
     * Get the instance of the plugin.
     * 
     * @return The plugin instance.
     */
    public static Ainstruments getInstance() {
        return instance;
    }

    /**
     * Get the instrument manager.
     * 
     * @return The instrument manager.
     */
    public InstrumentManager getInstrumentManager() {
        return instrumentManager;
    }
    
    /**
     * Get the VoiceChat integration.
     * 
     * @return The VoiceChat integration.
     */
    public VoicechatIntegration getVoicechatIntegration() {
        return voicechatIntegration;
    }
}
