# MTweet

MTweetは、Minecraft内からTwitterにツイートを送信することを目的にしたプラグインです。</br>
Skriptといった別プラグインから文章を連携し、1つのアカウントから呟かせることを想定して作成されています。</br>
ニコニコ生放送から情報を取得して、ツイート本文を作成することも可能です。</br>

# 対応バージョン
Spigot1.21.1以降対応です。

# 事前準備
1.Twitterの開発者登録をしたうえで、keys.ymlの以下の項目に設定を入れてください。</br>
　apiKey、apiKeySecret　→　Twitterの開発者登録後、API Key and Secret　の値をそれぞれ</br>
　oauthClientId、oauthClientSecret　→　上記同様Access Token and Secretの値</br>
　　　　　　　　　　　　　　　　　※権限はread and writeのトークンを発行してください。</br>
　tweetUserId　→ 上記で発行したTwitterのユーザーID</br>
</br>
これにて準備完了です。

# 使い方
・普通につぶやきたいとき</br>
　/mtweet tweet つぶやきたい本文</br>
　●置換可能文字列●</br>
　\n→改行になります。</br>
</br>
・ニコ生配信情報をつぶやきたいとき</br>
 /mtweet livetweet ニコニコユーザーID つぶやきたい本文</br>
　●置換可能文字列●</br>
　上記のほかに</br>
　[title]→ニコ生の配信タイトルが埋め込まれます</br>
　[liveUrl]→ニコ生の配信URLが埋め込まれます</br>

