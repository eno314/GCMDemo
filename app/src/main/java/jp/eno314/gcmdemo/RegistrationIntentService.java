package jp.eno314.gcmdemo;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * GCMからトークンが更新された通知を受け取った際の、トークン登録処理を行うIntentService
 *
 * Created by eno314 on 2015/06/07.
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = RegistrationIntentService.class.getSimpleName();

    private static final String[] TOPICS = {"global"};

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        try {
            // RegistrationIdの更新処理が複数件同時に発生する可能性を考慮して排他処理にする
            synchronized (TAG) {

                final InstanceID instanceID = InstanceID.getInstance(this);
                final String token;
                token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                Log.i(TAG, "GCM Registration Token: " + token);

                sendRegistrationToServer(token);

                subscribeTopics(token);

                // サーバーにトークンが送信されたかをプリファレンスに保管する
                // 既に登録されていればトークンはサーバーには送信する必要は無い
                preferences.edit().putBoolean("SENT_TOKEN_TO_SERVER", true).apply();
            }
        } catch (IOException e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // トークンの登録に失敗した際は、再度登録処理を行えるようにプリファレンスに失敗したことを保管する
            preferences.edit().putBoolean("SENT_TOKEN_TO_SERVER", false).apply();
        }
    }

    /**
     * 自作サーバーにGCMに登録されたトークンを送る
     */
    private void sendRegistrationToServer(String token) {
        // TODO 自作のアプリケーションサーバーに登録したトークンを送る
        // 自分たちで振り分けたIDをGCMに登録されたトークンを紐付けるために必要になるであろう処理
    }

    /**
     * トピックの購読設定
     */
    private void subscribeTopics(String token) throws IOException {
        for (String topic : TOPICS) {
            final GcmPubSub pubSub = GcmPubSub.getInstance(getApplicationContext());
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
}
