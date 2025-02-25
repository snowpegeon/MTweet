package com.github.snowpegeon.mtweet;

import com.github.snowpegeon.mtweet.command.MTweetCommand;
import com.github.snowpegeon.mtweet.command.MTweetTabCompleter;
import com.github.snowpegeon.mtweet.common.MTweetKeyConfig;
import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public final class MTweetPlugin extends JavaPlugin {

  /**
   * config.ymlを参照するFileConfigurationの実体.
   */
  public static FileConfiguration fileConfig;

  /**
   * key.ymlを参照するFileConfigurationの実体.
   */
  public static FileConfiguration keyConfig;

  /**
   * Pluginの実体.
   */
  public static JavaPlugin plugin;

  /**
   * コンストラクタ.
   */
  public MTweetPlugin(){plugin = this;}

  /**
   * プラグイン有効化時.
   */
  @Override
  public void onEnable() {
    getCommand("mtweet").setExecutor(new MTweetCommand(this));
    getCommand("mtweet").setTabCompleter(new MTweetTabCompleter());
    setup(this);
  }

  /**
   * 初期セットアップ.
   */
  public void setup(JavaPlugin plugin){

    // 通常configのセットアップ
    fileConfig = plugin.getConfig();
    fileConfig.options().copyDefaults(true);
    fileConfig.options().setHeader(List.of("MTweet Configuration"));
    plugin.saveConfig();

    // key.configのセットアップ
    MTweetKeyConfig keyConfigClass = new MTweetKeyConfig(plugin);
    keyConfig = keyConfigClass.getConfig();
    keyConfig.options().copyDefaults(true);
    // 特性上、何度も上書き処理を走らせたくないため、上書き制御付きのdefaultConfigを呼ぶ
    keyConfigClass.saveDefaultConfig();
  }
}
