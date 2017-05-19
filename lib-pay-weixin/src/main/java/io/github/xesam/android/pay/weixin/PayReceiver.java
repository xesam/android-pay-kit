package io.github.xesam.android.pay.weixin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by xesamguo@gmail.com on 17-5-19.
 */

public abstract class PayReceiver extends BroadcastReceiver {

    private static String RESULT_ACTION = "com.github.xesam.pay_result";
    private static String RESULT_EXTRA = "com.github.xesam.pay_result";

    public static void broadcastPayResult(Context context, WxPayResp wxPayResp) {
        Intent intent = new Intent(RESULT_ACTION);
        intent.putExtra(RESULT_EXTRA, wxPayResp);
        context.sendBroadcast(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        WxPayResp payResp = intent.getParcelableExtra(RESULT_EXTRA);
        onPayResult(context, intent, payResp);
    }

    public void register(Context context) {
        context.registerReceiver(this, new IntentFilter(RESULT_ACTION));
    }

    public void unregister(Context context) {
        context.unregisterReceiver(this);
    }

    public abstract void onPayResult(Context context, Intent intent, WxPayResp wxPayResp);
}
