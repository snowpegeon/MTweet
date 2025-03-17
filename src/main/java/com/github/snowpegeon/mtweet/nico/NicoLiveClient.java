package com.github.snowpegeon.mtweet.nico;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NicoLiveClient {
  // ニコニコ配信情報API　URL
  private static String nicoUrl = "";
  // ニコ生配信URL
  private static String liveUrl = "";

  private static int cacheTime = 1;

  private static HashMap<String, NicoLive> liveList = new HashMap<String, NicoLive>();

  public static void init(String nUrl, String lUrl, int cTime){
    nicoUrl =nUrl;
    liveUrl = lUrl;
    cacheTime = cTime;
  }

  private static NicoLive getLiveStreaming(String account) {

    NicoLive nicolive = new NicoLive();
    try {
      String url = nicoUrl.replaceAll("\\[account\\]", account);
      OkHttpClient client = new OkHttpClient();
      // リクエストの作成
      Request request = new Request.Builder().url(url).build();
      // レスポンスの取得
      Response response = client.newCall(request).execute();
      // レスポンスのBody要素を取得
      String responseBody = response.body().string();

      ObjectMapper mapper = new ObjectMapper();
      JsonNode root = mapper.readTree(responseBody);
      JsonNode program = root.get("data").get("programsList").get(0);

      nicolive.setUserId(account);
      nicolive.setLiveId(program.get("id").get("value").asText());
      nicolive.setTitle(program.get("program").get("title").asText());
      nicolive.setLargeThumbnail(program.get("thumbnail").get("listing").get("large").get("value").asText());
      nicolive.setUserName(program.get("programProvider").get("name").asText());
      nicolive.setUserIcon(program.get("programProvider").get("icons").get("uri150x150").asText());

      String lUrl = liveUrl.replaceAll("\\[liveId\\]", nicolive.getLiveId());
      nicolive.setLiveUrl(lUrl);

      nicolive.setCreatedAt(LocalDateTime.now());
    } catch(Exception e) {
      e.printStackTrace();
      return null;
    }

    return nicolive;
  }

  public static NicoLive getNicoLive(String account) {
    NicoLive nicolive = liveList.get(account);
    if(Objects.isNull(nicolive) || cacheTime <= Duration.between(nicolive.getCreatedAt(), LocalDateTime.now()).toMinutes()){
      nicolive = getLiveStreaming(account);
      liveList.put(account, nicolive);
    }

    return nicolive;
  }

  public static String getLiveId(String account){
    NicoLive nicolive = getNicoLive(account);

    return Objects.nonNull(nicolive) ? nicolive.getLiveId() : null;
  }

  public static String getLiveUrl(String account){
    NicoLive nicolive = getNicoLive(account);

    return Objects.nonNull(nicolive) ? nicolive.getLiveUrl() : null;
  }

  public static String getTitle(String account){
    NicoLive nicolive = getNicoLive(account);

    return Objects.nonNull(nicolive) ? nicolive.getTitle() : null;
  }

  public static String getUserName(String account){
    NicoLive nicolive = getNicoLive(account);

    return Objects.nonNull(nicolive) ? nicolive.getUserName() : null;
  }

  public static String getUserId(String account){
    NicoLive nicolive = getNicoLive(account);

    return Objects.nonNull(nicolive) ? nicolive.getUserId() : null;
  }

  public static String getUserIcon(String account){
    NicoLive nicolive = getNicoLive(account);

    return Objects.nonNull(nicolive) ? nicolive.getUserIcon() : null;
  }

  public static String getLargeThumbnail(String account){
    NicoLive nicolive = getNicoLive(account);

    return Objects.nonNull(nicolive) ? nicolive.getLargeThumbnail() : null;
  }

}
