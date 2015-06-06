package jp.eno314.gcmdemo;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 *
 * Created by eno314 on 2015/06/07.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = MyInstanceIDListenerService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        Log.d(TAG, "onTokenRefresh");

        // GCMからトークンが更新された通知を受け取って、自作サーバーへの登録処理を行う。
        final Intent intent = new Intent(getApplicationContext(), RegistrationIntentService.class);
        startService(intent);
    }
}
