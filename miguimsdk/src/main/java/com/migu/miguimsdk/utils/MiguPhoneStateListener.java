package com.migu.miguimsdk.utils;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.migu.miguimsdk.MiguIMSDK;
import com.zego.zegoliveroom.constants.ZegoConstants;

/**
 * Created by ZY on 2018/4/16.
 */

public class MiguPhoneStateListener extends PhoneStateListener {

    private boolean mHostHasBeenCalled;
    private MiguIMSDK mZegoLiveRoom;

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                if (mHostHasBeenCalled) {
                    mHostHasBeenCalled = false;
                    MiguIMLogUtil.e( "CALL_STATE_IDLE : call state idle");
                    // 结束通话，恢复音频模块
                    mZegoLiveRoom.resumeModule(ZegoConstants.ModuleType.AUDIO);
                }

                break;
            case TelephonyManager.CALL_STATE_RINGING:
                MiguIMLogUtil.e( "CALL_STATE_RINGING : call state ringing");
                mHostHasBeenCalled = true;
                // 来电，暂停音频模块
                mZegoLiveRoom.pauseModule(ZegoConstants.ModuleType.AUDIO);
                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
                break;
        }
    }

}
