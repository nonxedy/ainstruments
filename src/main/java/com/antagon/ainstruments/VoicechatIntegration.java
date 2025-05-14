package com.antagon.ainstruments;

import org.bukkit.plugin.Plugin;

import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;

/**
 * Integration with the Simple Voice Chat API.
 */
public class VoicechatIntegration implements VoicechatPlugin {
    
    private final Ainstruments plugin;
    private VoicechatApi voicechatApi;
    
    public VoicechatIntegration() {
        this.plugin = Ainstruments.getInstance();
    }
    
    @Override
    public String getPluginId() {
        return "ainstruments";
    }
    
    @Override
    public void initialize(VoicechatApi api) {
        this.voicechatApi = api;
        plugin.getLogger().info("VoiceChat API initialized");
    }
    
    /**
     * Get the voicechat API.
     * 
     * @return the voicechat API
     */
    public VoicechatApi getVoicechatApi() {
        return voicechatApi;
    }
    
    /**
     * Check if voicechat is enabled and available.
     * 
     * @return true if voicechat is enabled, false otherwise
     */
    public boolean isVoicechatEnabled() {
        return voicechatApi != null;
    }
}
