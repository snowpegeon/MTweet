package com.github.snowpegeon.mtweet.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.snowpegeon.mtweet.common.MTweetClient;
import com.github.snowpegeon.mtweet.MTweetPlugin;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.signature.TwitterCredentials;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Logger;
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

  // ニコニコ配信情報API　URL
  private String nicoUrl = "https://live.nicovideo.jp/front/api/v1/user-broadcast-history?providerId=[account]&providerType=user&limit=1";
  // ニコ生配信URL
  private String liveUrl = "https://live.nicovideo.jp/watch/[liveId]";

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
      boolean res = false;
      // 通常ツイート処理
      if(strings[0].equalsIgnoreCase("tweet")){
        String[] strLists = Arrays.copyOfRange(strings, 1, strings.length);
        String str = String.join(" ", strLists);
        res = sendTweet(str);
        if(res){
          logger.info("ツイート送信に成功しました。");
        } else {
          logger.severe("ツイート送信に失敗しました。");
        }
      // ニコ生ツイート処理
      } else if(strings[0].equalsIgnoreCase("livetweet")){
        String liveAccount = strings[1];
        String[] strLists = Arrays.copyOfRange(strings, 2, strings.length);
        String str = String.join(" ", strLists);
        res = sendLiveTweet(liveAccount, str);
        if(res){
          logger.info("ニコ生ツイート送信に成功しました。");
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
  private boolean sendTweet(String text){
    String apiKey = plugin.keyConfig.getString("apiKey");
    String apiKeySecret = plugin.keyConfig.getString("apiKeySecret");
    String oauthClientId = plugin.keyConfig.getString("oauthClientId");
    String oauthClientSecret = plugin.keyConfig.getString("oauthClientSecret");

    TwitterCredentials credential;
    boolean res = false;
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

      client.postTweet(text);
      res = true;
    } catch (Exception e) {
      logger.severe("ツイート生成処理に失敗しました。");
      e.printStackTrace();
    }
    return res;
  }

  /**
   * ニコ生ツイート送信処理.
   */
  private boolean sendLiveTweet(String account, String str) {

    String liveId = "";
    String title = "";
    try {
      String url = nicoUrl.replaceAll("\\[account\\]", account);
      OkHttpClient client = new OkHttpClient();
      // リクエストの作成
      Request request = new Request.Builder().url(url).build();
      // レスポンスの取得
      Response response = client.newCall(request).execute();
      // レスポンスのBody要素を取得
      String responseBody = response.body().string();
      logger.info(responseBody);

      ObjectMapper mapper = new ObjectMapper();
      JsonNode root = mapper.readTree(responseBody);
      JsonNode program = root.get("data").get("programsList").get(0);
      liveId = program.get("id").get("value").asText();
      title = program.get("program").get("title").asText();

      logger.info(liveId);
      logger.info(title);
    } catch(Exception e) {
      logger.severe("ニコ生情報取得に失敗しました。");
      e.printStackTrace();
      return false;
    }
    if(liveId.isEmpty() || title.isEmpty()) {
      logger.severe("ニコ生配信詳細情報取得に失敗しました。");
      return false;
    }

    String lUrl = liveUrl.replaceAll("\\[liveId\\]", liveId);
    String tweet = str.replaceAll("\\[liveUrl\\]", lUrl);
    tweet = tweet.replaceAll("\\[title\\]", title);
    return sendTweet(tweet);
  }
}
