package com.github.snowpegeon.mtweet.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * key.config用セットアップクラス.
 */
public class MTweetKeyConfig {

  private FileConfiguration config = null;
  private File configFile;
  private String file;
  private Plugin plugin;


  /**
   * Pluginだけのコンストラクタ.
   */
  public MTweetKeyConfig(Plugin plugin) {
    this(plugin, "keys.yml");
  }

  /**
   * コンストラクタ.
   */
  private MTweetKeyConfig(Plugin plugin, String fileName) {
    this.plugin = plugin;
    this.file = fileName;

    // plugin.ymlから、ファイルパスを受け取って設定場所を変更する
    FileConfiguration config = plugin.getConfig();
    String path = config.getString("keyPass");

    configFile = new File(path, file);
  }

  /**
   * デフォルトのConfigファイルの保存.
   */
  public void saveDefaultConfig() {
    if (!configFile.exists()) {
      saveConfig();
    }
  }

  /**
   * Configファイルの再読み込み.
   */
  public void reloadConfig() {
    config = YamlConfiguration.loadConfiguration(configFile);

    final InputStream defConfigStream = plugin.getResource(file);
    if (defConfigStream == null) {
      return;
    }

    config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
  }

  /**
   * Configファイル取得.
   */
  public FileConfiguration getConfig() {
    if (config == null) {
      reloadConfig();
    }
    return config;
  }
  public void saveConfig() {
    if (config == null) {
      return;
    }
    try {
      getConfig().save(configFile);
    } catch (IOException ex) {
      plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
    }
  }
}
