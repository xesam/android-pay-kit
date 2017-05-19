package io.github.xesam.android.pay.weixin;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xesamguo@gmail.com on 17-5-19.
 */

public class WxPayResp implements Parcelable {
    public String prepayId;
    public int errCode;

    public WxPayResp() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.prepayId);
        dest.writeInt(this.errCode);
    }

    protected WxPayResp(Parcel in) {
        this.prepayId = in.readString();
        this.errCode = in.readInt();
    }

    public static final Creator<WxPayResp> CREATOR = new Creator<WxPayResp>() {
        @Override
        public WxPayResp createFromParcel(Parcel source) {
            return new WxPayResp(source);
        }

        @Override
        public WxPayResp[] newArray(int size) {
            return new WxPayResp[size];
        }
    };
}
