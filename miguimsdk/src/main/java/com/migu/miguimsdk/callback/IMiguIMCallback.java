package com.migu.miguimsdk.callback;


import com.migu.miguimsdk.entity.MiguBigRoomMessage;
import com.migu.miguimsdk.entity.MiguConversationMessage;
import com.migu.miguimsdk.entity.MiguRoomMessage;
import com.migu.miguimsdk.entity.MiguUserState;

/**
 * Created by ZY on 2018/4/16.
 */

public interface IMiguIMCallback  {
    void onUserUpdate(MiguUserState[] var1, int var2);

    void onRecvRoomMessage(String var1, MiguRoomMessage[] var2);

    void onRecvConversationMessage(String var1, String var2, MiguConversationMessage var3);

    void onUpdateOnlineCount(String var1, int var2);

    void onRecvBigRoomMessage(String var1, MiguBigRoomMessage[] var2);

}
