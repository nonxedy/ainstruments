package com.antagon;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class Ainstruments extends JavaPlugin {
  private static final Logger LOGGER=Logger.getLogger("ainstruments");

  public void onEnable() {
    LOGGER.info("ainstruments enabled");
  }

  public void onDisable() {
    LOGGER.info("ainstruments disabled");
  }
}
