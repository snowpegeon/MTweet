package com.github.snowpegeon.mtweet.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.snowpegeon.mtweet.common.MTweetClient;
import com.github.snowpegeon.mtweet.MTweetPlugin;
import com.github.snowpegeon.mtweet.nico.NicoLiveClient;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.signature.TwitterCredentials;
import io.github.redouane59.twitter.dto.tweet.Tweet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * MTweetのコマンド設定用Executer.
 */
public class MTweetCommand implements CommandExecutor {
  public MTweetPlugin plugin;
  public Logger logger;
  public HashMap<String,TwitterCredentials> credentials = new HashMap<>();

  private Pattern numRegex = Pattern.compile("^[0-9]{1,9}$");

  /**
   * コンストラクタ.
   */
  public MTweetCommand(MTweetPlugin plugin) {
    this.plugin = plugin;
    this.logger = plugin.getLogger();
  }

  /**
   * コマンド実行処理本体.
   */
  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String label, String[] strings) {
    logger.info("<< onCommand START >>");
    // labelがmtweetなら処理をする
    if (label.equalsIgnoreCase("mtweet")) {
      String res = null;
      // 通常ツイート処理
      if(strings[0].equalsIgnoreCase("tweet")){
        String[] strLists = Arrays.copyOfRange(strings, 1, strings.length);
        String str = String.join(" ", strLists);
        // 文字列設定チェック
        if(str.length() == 0){
          commandSender.sendMessage("ツイート本文が設定されていません。設定した上で再度コマンドを実行してください。");
          return true;
        }
        res = sendTweet(str);
        if(Objects.nonNull(res)){
          logger.info("ツイート送信に成功しました。");
          TextComponent component = new TextComponent("投稿ポスト（X）はこちら");
          component.setUnderlined(true);
          component.setColor(ChatColor.GOLD);
          component.setClickEvent(new ClickEvent(Action.OPEN_URL, res));
          commandSender.spigot().sendMessage(component);
        } else {
          logger.severe("ツイート送信に失敗しました。");
        }
      // ニコ生ツイート処理
      } else if(strings[0].equalsIgnoreCase("livetweet")){
        String liveAccount = strings[1];
        // ニコニコユーザーID形式チェック
        if(!numRegex.matcher(liveAccount).matches()){
          commandSender.sendMessage("ニコニコユーザーIDの形式が誤っています。数字のみ。1～9桁の範囲におさまっているかをご確認ください。");
          return true;
        }

        String[] strLists = Arrays.copyOfRange(strings, 2, strings.length);
        String str = String.join(" ", strLists);
        // 文字列設定チェック
        if(str.length() == 0){
          commandSender.sendMessage("ツイート本文が設定されていません。設定した上で再度コマンドを実行してください。");
          return true;
        }
        res = sendLiveTweet(liveAccount, str);
        if(Objects.nonNull(res)){
          logger.info("ニコ生ツイート送信に成功しました。");
          TextComponent component = new TextComponent("放送通知ポスト（X）はこちら");
          component.setUnderlined(true);
          component.setColor(ChatColor.GOLD);
          component.setClickEvent(new ClickEvent(Action.OPEN_URL, res));
          commandSender.spigot().sendMessage(component);
        } else {
          logger.severe("ニコ生ツイート送信に失敗しました。");
        }
      }
    }
    // テストコマンド
    logger.info("<< onCommand END >>");
    return true;
  }

  /**
   * ツイート送信処理.
   */
  private String sendTweet(String text){
    String apiKey = plugin.keyConfig.getString("apiKey");
    String apiKeySecret = plugin.keyConfig.getString("apiKeySecret");
    String oauthClientId = plugin.keyConfig.getString("oauthClientId");
    String oauthClientSecret = plugin.keyConfig.getString("oauthClientSecret");
    String tweetUserId = plugin.keyConfig.getString("tweetUserId");
    String tweetUrl = plugin.keyConfig.getString("tweetUrl");

    TwitterCredentials credential;
    String res = null;
    try {
      if(Objects.isNull(credentials.get(oauthClientId))){
        credentials.put(oauthClientId,
            TwitterCredentials.builder()
                .apiKey(apiKey)
                .apiSecretKey(apiKeySecret)
                .accessToken(oauthClientId)
                .accessTokenSecret(oauthClientSecret).build());
      }
      credential = credentials.get(oauthClientId);

      TwitterClient client = new MTweetClient(credential);

      Tweet tweet = client.postTweet(text);
      String tweetId = tweet.getId();
      if(Objects.nonNull(tweetId)) {
        res = tweetUrl.replaceAll("\\[tweetUserId\\]", tweetUserId).replaceAll("\\[tweetId\\]", tweetId);
      }
    } catch (Exception e) {
      logger.severe("ツイート生成処理に失敗しました。");
      e.printStackTrace();
    }
    return res;
  }

  /**
   * ニコ生ツイート送信処理.
   */
  private String sendLiveTweet(String account, String str) {

    // ニコ生情報取得
    String liveUrl = NicoLiveClient.getLiveUrl(account);
    String title = NicoLiveClient.getTitle(account);
    if(Objects.isNull(liveUrl) || Objects.isNull(title)) {
      logger.severe("ニコ生配信詳細情報取得に失敗しました。後ほどお試しください。");
      return null;
    }

    // ツイート本文作成並びに送信
    String tweet = str.replaceAll("\\[liveUrl\\]", liveUrl);
    tweet = tweet.replaceAll("\\[title\\]", title);
    return sendTweet(tweet);
  }
}
