package com.migu.miguimsdk.callback;


import com.migu.miguimsdk.entity.MiguStreamInfo;

/**
 * Created by ZY on 2018/4/16.
 */

public interface IMiguRoomCallback {
    void onKickOut(int var1, String var2);

    void onDisconnect(int var1, String var2);

    void onReconnect(int var1, String var2);

    void onTempBroken(int var1, String var2);

    void onStreamUpdated(int var1, MiguStreamInfo[] var2, String var3);

    void onStreamExtraInfoUpdated(MiguStreamInfo[] var1, String var2);

    void onRecvCustomCommand(String var1, String var2, String var3, String var4);
}
