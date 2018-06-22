package com.migu.miguimsdk.entity;

import com.zego.zegoliveroom.entity.ZegoConversationInfo;
import com.zego.zegoliveroom.entity.ZegoUser;

/**
 * Created by ZY on 2018/4/17.
 */

public class MiguConversationInfo {
    public String conversationName;
    public String creatorID;
    public long createTime;
    public ZegoUser[] listMember;

    public MiguConversationInfo() {
    }
}
