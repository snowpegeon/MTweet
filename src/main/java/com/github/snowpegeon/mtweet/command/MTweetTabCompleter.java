package com.github.snowpegeon.mtweet.command;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

/**
 * MTweet　コマンド打つ時の候補リスト作成クラス.
 */
public class MTweetTabCompleter implements TabCompleter {

  /**
   * コマンド打つ時の候補リスト作成.
   */
  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){

    // MTweetのものか確認
    if(command.getName().equalsIgnoreCase("mtweet")) {
      // プレイヤー以外には送らない
      if(sender instanceof Player) {
        Player p = (Player) sender;
        List<String> complete = new ArrayList<String>();
        // 1つめの引数なら、機能の候補を出す
        if(args.length == 1) {
          complete.add("tweet");
          complete.add("livetweet");
          return complete;
        }

        // tweetの場合
        if(args[0].equalsIgnoreCase("tweet") && args.length == 2) {
          complete.add("tweetText");
          return complete;
        }
        // livetweetの場合
        if(args[0].equalsIgnoreCase("livetweet")) {
          // 2番目ならニコニコIDを催促
          if(args.length == 2) {
            complete.add("nicoId");
            return complete;
            // 3番目ならツイート本文を催促
          } else if (args.length == 3) {
            complete.add("tweetText");
            return complete;
          }
        }
      }
    }
    return null;
  }
}
