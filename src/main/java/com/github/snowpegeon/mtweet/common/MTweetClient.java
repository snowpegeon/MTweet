package com.github.snowpegeon.mtweet.common;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.tweet.Tweet;
import io.github.redouane59.twitter.dto.tweet.TweetParameters;
import io.github.redouane59.twitter.dto.tweet.TweetV2;
import io.github.redouane59.twitter.helpers.JsonHelper;
import io.github.redouane59.twitter.signature.TwitterCredentials;
import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * MTweet用クライアント.
 */
public class MTweetClient extends TwitterClient {

  /**
   * コンストラクタ.
   */
  public MTweetClient(TwitterCredentials credentials) {
    super(credentials);
  }

  /**
   * ツイート送信.
   */
  public Tweet postTweet(String text) {
    return this.postTweet(TweetParameters.builder().text(text).build());
  }

  /**
   * ツイート送信.
   */
  public Tweet postTweet(TweetParameters tweetParameters) {
    try {
      String url = this.getUrlHelper().getPostTweetUrl();
      String body = JsonHelper.toJson(tweetParameters);

      // ツイッターで受け付けられるはずの改行コードが勝手にパース置換されてしまうため、それを解除する
      body = body.replaceAll("\\\\n", "\\n");
      this.setAutomaticRetry(false);
      return (Tweet)this.getRequestHelperV1().postRequestWithBodyJson(url, new HashMap(), body, TweetV2.class).orElseThrow(
          NoSuchElementException::new);
    } catch (Throwable var4) {
      Throwable $ex = var4;
    }
    return null;
  }
}
