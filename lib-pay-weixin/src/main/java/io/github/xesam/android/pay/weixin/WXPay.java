package io.github.xesam.android.pay.weixin;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPay implements IWXAPIEventHandler {

    private IWXAPI mApi = null;
    private Context mContext = null;

    private static WXPay INSTANCE = null;

    public static WXPay instance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new WXPay(context.getApplicationContext());
        }
        return INSTANCE;
    }

    public WXPay(Context context) {
        this.mContext = context;
    }

    private boolean checkWXPayBean(WXPayBean bean) {
        return bean != null
                && !TextUtils.isEmpty(bean.nonceStr)
                && !TextUtils.isEmpty(bean.prepayId)
                && !TextUtils.isEmpty(bean.partnerId)
                && !TextUtils.isEmpty(bean.sign)
                && !TextUtils.isEmpty(bean.timestamp);
    }

    public void pay(WXPayBean bean, PayListener payListener) {
        boolean check = checkWXPayBean(bean);
        if (!check) {
            return;
        }
        mApi = WXAPIFactory.createWXAPI(mContext, bean.appId);
        mApi.registerApp(bean.appId);
        boolean isPaySupported = mApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {
            if (payListener != null) {
                payListener.onError();
            }
        } else {
            PayReq req = new PayReq();
            req.appId = bean.appId;
            req.partnerId = bean.partnerId;
            req.prepayId = bean.prepayId;
            req.nonceStr = bean.nonceStr;
            req.timeStamp = bean.timestamp;
            req.packageValue = "Sign=WXPay";
            req.sign = bean.sign;
            mApi.sendReq(req);
            if (payListener != null) {
                payListener.onSuccess();
            }
        }
    }

    public void finish(Intent intent) {
        mApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            PayResp payResp = (PayResp) resp;
            WxPayResp wxPayResp = new WxPayResp();
            wxPayResp.errCode = resp.errCode;
            wxPayResp.prepayId = payResp.prepayId;
            PayReceiver.broadcastPayResult(mContext, wxPayResp);
        }
    }
}
