package com.migu.miguimsdk;

import android.app.Service;
import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.migu.miguimsdk.callback.IMiguBigRoomMessageCallback;
import com.migu.miguimsdk.callback.IMiguConversationMessageCallback;
import com.migu.miguimsdk.callback.IMiguCreateConversationCallback;
import com.migu.miguimsdk.callback.IMiguGetConversationInfoCallback;
import com.migu.miguimsdk.callback.IMiguIMCallback;
import com.migu.miguimsdk.callback.IMiguLoginCompletionCallback;
import com.migu.miguimsdk.callback.IMiguRoomCallback;
import com.migu.miguimsdk.callback.IMiguRoomMessageCallback;
import com.migu.miguimsdk.entity.MiguBigRoomMessage;
import com.migu.miguimsdk.entity.MiguConversationMessage;
import com.migu.miguimsdk.entity.MiguRoomMessage;
import com.migu.miguimsdk.entity.MiguStreamInfo;
import com.migu.miguimsdk.entity.MiguUserState;
import com.migu.miguimsdk.utils.MiguIMLogUtil;
import com.migu.miguimsdk.utils.MiguPhoneStateListener;
import com.zego.zegoavkit2.ZegoVideoCaptureFactory;
import com.zego.zegoavkit2.videofilter.ZegoVideoFilterFactory;
import com.zego.zegoliveroom.ZegoLiveRoom;
import com.zego.zegoliveroom.callback.IZegoLoginCompletionCallback;
import com.zego.zegoliveroom.callback.IZegoRoomCallback;
import com.zego.zegoliveroom.callback.im.IZegoBigRoomMessageCallback;
import com.zego.zegoliveroom.callback.im.IZegoIMCallback;
import com.zego.zegoliveroom.callback.im.IZegoRoomMessageCallback;
import com.zego.zegoliveroom.constants.ZegoConstants;
import com.zego.zegoliveroom.constants.ZegoIM;
import com.zego.zegoliveroom.entity.ZegoBigRoomMessage;
import com.zego.zegoliveroom.entity.ZegoConversationMessage;
import com.zego.zegoliveroom.entity.ZegoRoomMessage;
import com.zego.zegoliveroom.entity.ZegoStreamInfo;
import com.zego.zegoliveroom.entity.ZegoUserState;

/**
 * Created by ZY on 2018/4/16.
 */

public final class MiguIMSDK {

    private ZegoLiveRoom g_ZegoApi;
    private static MiguIMSDK miGuIMSDK;

    public MiguIMSDK() {
        this.g_ZegoApi = new ZegoLiveRoom();
    }

    /**
     * 获取单例
     * @return
     */
    public static MiguIMSDK getInstance() {
        synchronized (MiguIMSDK.class) {
            if (miGuIMSDK == null) {
                miGuIMSDK = new MiguIMSDK();
            }
            return miGuIMSDK;
        }
    }


    /**
     * 测试环境开关
     *
     * @param useTestEnv
     */

    public static void setTestEnv(boolean useTestEnv) {
        ZegoLiveRoom.setTestEnv(useTestEnv);
    }

    /**
     * 根据当前运行模式是否打开调试信息
     */
    public static void setVerbose() {
        ZegoLiveRoom.setVerbose(BuildConfig.DEBUG);
    }

    /**
     * 设置 UserID 和 UserName。userID 和 userName 来自于 App 自定义的账号系统
     *

     * @return
     */
    public static boolean setUser(String userID, String userName) {
        if (TextUtils.isEmpty(userID)) {
            MiguIMLogUtil.e("[Java_ZegoLiveRoom_setUser] failed, userID is empty");
            return false;
        } else if (TextUtils.isEmpty(userName)) {
            MiguIMLogUtil.e("[Java_ZegoLiveRoom_setUser] failed, userName is empty");
            return false;
        } else {
            return ZegoLiveRoom.setUser(userID, userName);
        }
    }

    /**
     * 初始化sdk
     * @param userID
     * @param userName
     * @param appID
     * @param appSign
     * @param context
     * @return
     */
    public boolean initSDK(String userID, String userName,long appID, byte[] appSign, Context context) {
        boolean reBoolean;
        if (context == null) {
            MiguIMLogUtil.e("[Java_MiGuIM_initSDK] failed, context is null");
            reBoolean =  false;
        } else {

            if (TextUtils.isEmpty(userID)) {
                MiguIMLogUtil.e("[Java_ZegoLiveRoom_setUser] failed, userID is empty");
                reBoolean =  false;
            } else if (TextUtils.isEmpty(userName)) {
                MiguIMLogUtil.e("[Java_ZegoLiveRoom_setUser] failed, userName is empty");
                reBoolean =  false;
            } else {
                reBoolean = ZegoLiveRoom.setUser(userID, userName);
            }
            reBoolean =  g_ZegoApi.initSDK(appID, appSign, context);
        }
        return reBoolean;
    }

    /**
     * 取消初始化
     * @return
     */
    public boolean unInitSDK() {
        return this.g_ZegoApi.unInitSDK();
    }




    /**
     * 设置房间配置信息。
     *
     * @param audienceCreateRoom 观众是否可以创建房间：true: 可以，false: 不可以。默认值为 true
     * @param userStateUpdate    用户状态（进入/退出房间））是否广播：
     *                           true:  房间内用户状态改变时，其他用户会收到
     *                           false: 房间内用户状态改变时，其他用户不会收到
     */
    public void setRoomConfig(boolean audienceCreateRoom, boolean userStateUpdate){
        g_ZegoApi.setRoomConfig(audienceCreateRoom,userStateUpdate);
    }


    /**
     * 建立长连接。
     *
     * @param roomID   节目id ID，长度 <= 255 bytes 的可打印字符串
     * @param callback 实现 {@link IMiguLoginCompletionCallback} 接口的对象实例，用于接收登录结果
     * @return true: 调用成功，false: 调用失败
     */
    public boolean connection(String roomID, IMiguLoginCompletionCallback callback) {
        return connection(roomID,"",callback);
    }

    /**
     * 登录房间。
     *
     * @param roomID   房间 ID，长度 <= 255 bytes 的可打印字符串
     * @param roomName 房间名字，长度 <= 255 bytes 的可见字符串
     * @param callback 实现 {@link IMiguLoginCompletionCallback} 接口的对象实例，用于接收登录结果
     * @return true: 调用成功，false: 调用失败
     */
    public boolean connection(String roomID, String roomName, final IMiguLoginCompletionCallback callback) {
        if(TextUtils.isEmpty(roomID)) {
            MiguIMLogUtil.e("[Java_MiGuIM_loginRoom] failed, roomID is empty");
            return false;
        }
        if(TextUtils.isEmpty(roomName)) {
            roomName = "";
        }
        if(callback == null) {
            MiguIMLogUtil.e("[Java_MiGuIM_loginRoom] failed, callback is null");
            return false;
        }
        return g_ZegoApi.loginRoom(roomID, roomName, 2, new IZegoLoginCompletionCallback() {
            @Override
            public void onLoginCompletion(int i, ZegoStreamInfo[] zegoStreamInfos) {
                MiguStreamInfo[] miguStreamInfos = new MiguStreamInfo[zegoStreamInfos.length];
                for(int index = 0; index < zegoStreamInfos.length; ++index) {
                    miguStreamInfos[index] = zegoStreamInfoToMigu(zegoStreamInfos[index]);
                }
                callback.onLoginCompletion(i,miguStreamInfos);
            }
        });
    }

    /**
     * 长连接状态监听
     * @param callback
     */
    public void setMiGuRoomCallback(final IMiguRoomCallback callback){
        g_ZegoApi.setZegoRoomCallback(new IZegoRoomCallback() {
            @Override
            public void onKickOut(int i, String s) {
                callback.onKickOut(i,s);
            }

            @Override
            public void onDisconnect(int i, String s) {
                callback.onDisconnect(i,s);
            }

            @Override
            public void onReconnect(int i, String s) {
                callback.onReconnect(i,s);
            }

            @Override
            public void onTempBroken(int i, String s) {
                callback.onTempBroken(i,s);
            }

            @Override
            public void onStreamUpdated(int type, ZegoStreamInfo[] zegoStreamInfos, String roomID) {
                MiguStreamInfo[] miguStreamInfos = new MiguStreamInfo[zegoStreamInfos.length];

                for(int index = 0; index < zegoStreamInfos.length; ++index) {
                    miguStreamInfos[index] = zegoStreamInfoToMigu(zegoStreamInfos[index]);
                }

                if(callback != null) {
                    callback.onStreamUpdated(type, miguStreamInfos, roomID);
                }
            }


            public void onStreamExtraInfoUpdated(ZegoStreamInfo[] zegoStreamInfos, String s) {
                MiguStreamInfo[] miguStreamInfos = new MiguStreamInfo[zegoStreamInfos.length];

                for(int index = 0; index < zegoStreamInfos.length; ++index) {
                    miguStreamInfos[index] = zegoStreamInfoToMigu(zegoStreamInfos[index]);
                }

                if(callback != null) {
                    callback.onStreamExtraInfoUpdated(miguStreamInfos, s);
                }

            }

            public void onRecvCustomCommand(String s, String s1, String s2, String s3) {
                if(callback != null) {
                    callback.onRecvCustomCommand(s, s1, s2, s3);
                }

            }
        });
    }

    /**
     * 断开长连接
     * @return
     */
    public boolean disConnection(){
        return g_ZegoApi.logoutChatRoom();
    }

    /**
     * 设置 IM 回调接口，用于接收即时通信内容。
     *
     * @param callback 实现了 {@link IMiguIMCallback} 接口的对象实例，用于接收即时通信内容
     */
    public void setMiGuIMCallback(final IMiguIMCallback callback){
        g_ZegoApi.setZegoIMCallback(new IZegoIMCallback() {
            public void onUserUpdate(ZegoUserState[] zegoUserStates, int i) {
                MiguUserState[] userState = new MiguUserState[zegoUserStates.length];

                for(int index = 0; index < zegoUserStates.length; ++index) {
                    MiguUserState state = new MiguUserState();
                    state.roomRole = zegoUserStates[index].roomRole;
                    state.updateFlag = zegoUserStates[index].updateFlag;
                    state.userName = zegoUserStates[index].userName;
                    state.userID = zegoUserStates[index].userID;
                    userState[index] = state;
                }

                if(callback != null) {
                    callback.onUserUpdate(userState, i);
                }

            }

            public void onRecvRoomMessage(String s, ZegoRoomMessage[] zegoRoomMessages) {
                MiguRoomMessage[] roomMessage = new MiguRoomMessage[zegoRoomMessages.length];

                for(int index = 0; index < zegoRoomMessages.length; ++index) {
                    MiguRoomMessage msg = new MiguRoomMessage();
                    msg.content = zegoRoomMessages[index].content;
                    msg.fromUserID = zegoRoomMessages[index].fromUserID;
                    msg.fromUserName = zegoRoomMessages[index].fromUserName;
                    msg.messageID = zegoRoomMessages[index].messageID;
                    msg.messageType = zegoRoomMessages[index].messageType;
                    msg.messageCategory = zegoRoomMessages[index].messageCategory;
                    msg.messagePriority = zegoRoomMessages[index].messagePriority;
                    roomMessage[index] = msg;
                }

                if(callback != null) {
                    callback.onRecvRoomMessage(s, roomMessage);
                }

            }

            public void onRecvConversationMessage(String s, String s1, ZegoConversationMessage zegoConversationMessage) {
                MiguConversationMessage conversationMessage = new MiguConversationMessage();
                conversationMessage.fromUserID = zegoConversationMessage.fromUserID;
                conversationMessage.fromUserName = zegoConversationMessage.fromUserName;
                conversationMessage.messageID = zegoConversationMessage.messageID;
                conversationMessage.content = zegoConversationMessage.content;
                conversationMessage.messageType = zegoConversationMessage.messageType;
                conversationMessage.sendTime = zegoConversationMessage.sendTime;
                if(callback != null) {
                    callback.onRecvConversationMessage(s, s1, conversationMessage);
                }

            }

            public void onUpdateOnlineCount(String s, int i) {
                if(callback != null) {
                    callback.onUpdateOnlineCount(s, i);
                }

            }

            @Override
            public void onRecvBigRoomMessage(String s, ZegoBigRoomMessage[] zegoBigRoomMessages) {
                MiguBigRoomMessage[] bigRoomMessages = new MiguBigRoomMessage[zegoBigRoomMessages.length];
                for(int index = 0; index < zegoBigRoomMessages.length; ++index) {
                    MiguBigRoomMessage msg = new MiguBigRoomMessage();
                    msg.fromUserID = zegoBigRoomMessages[index].fromUserID;
                    msg.fromUserName = zegoBigRoomMessages[index].fromUserName;
                    msg.messageID = zegoBigRoomMessages[index].messageID;
                    msg.content = zegoBigRoomMessages[index].content;
                    msg.messageType = zegoBigRoomMessages[index].messageType;
                    msg.messageCategory = zegoBigRoomMessages[index].messageCategory;
                    bigRoomMessages[index] = msg;
                }
                if(callback != null) {
                    callback.onRecvBigRoomMessage(s, bigRoomMessages);
                }
            }
        });
    }

    /**
     * 发送房间内广播消息。
     *
     * <p>发送成功后，房间内其他成员会通过 {@link IMiguIMCallback#onRecvRoomMessage(String, MiguRoomMessage[])} 接收此消息</p>
     *
     * @param messageType       消息类型，   详见 {@link com.migu.miguimsdk.constants.MiguIM.MessageType}
     * @param messageCategory   消息分类，   详见 {@link com.migu.miguimsdk.constants.MiguIM.MessageCategory}
     * @param messagePriority   消息优先级， 详见 {@link com.migu.miguimsdk.constants.MiguIM.MessagePriority}
     * @param content           消息内容，长度 <= 1024 bytes 的可打印字符串
     * @param callback          实现 {@link IMiguRoomMessageCallback} 接口的对象实例，用于接收消息发送结果及 server 下发的 messageID
     * @return                  true:调用成功 等待 {@link IMiguRoomMessageCallback#onSendRoomMessage(int, String, long)} 返回, false:调用失败
     */
    boolean sendRoomMessage(int messageType, int messageCategory, int messagePriority, String content, final IMiguRoomMessageCallback callback){
        return g_ZegoApi.sendRoomMessage(messageType, messageCategory, messagePriority, content, new IZegoRoomMessageCallback() {
            @Override
            public void onSendRoomMessage(int i, String s, long l) {
                if (callback!=null){
                    callback.onSendRoomMessage(i,s,l);
                }
            }
        });
    }

    public boolean sendBigRoomMessage( String content,final IMiguBigRoomMessageCallback callback){
        return g_ZegoApi.sendBigRoomMessage(ZegoIM.MessageType.Text, ZegoIM.MessageCategory.Chat, content, new IZegoBigRoomMessageCallback() {
            @Override
            public void onSendBigRoomMessage(int i, String s, String s1) {
                if (callback!=null){
                    callback.onSendBigRoomMessage(i,s,s1);
                }
            }
        });
    }
//
//    /**
//     * 收到房间的广播消息。
//     *
//     * @param roomID        房间 ID
//     * @param listMsg       消息列表, 每条消息都将包含消息内容，消息分类，消息类型，发送者等信息
//     * @see com.migu.miguimsdk.ZegoLiveRoom#sendRoomMessage(int, int, int, String, IZegoRoomMessageCallback)
//     */
//    void onRecvRoomMessage(String roomID, MiguRoomMessage[] listMsg){
//        g_ZegoApi.onRecvRoomMessage(roomID,listMsg);
//    }
//
//
//    /**
//     * 在房间中创建一个会话。
//     * 要想观众可以创建房间，必须在登录前调用 {@link #setRoomConfig(boolean, boolean)} 进行设置。
//     *
//     * @param conversationName     会话名称，长度 <= 255 bytes 的可打印字符串
//     * @param listMember           参与会话的成员列表，所有成员必须在同一个房间内
//     * @param callback             实现 {@link IZegoCreateConversationCallback} 接口的对象实例，用于接收创建会话结果及 server 下发的会话 ID
//     * @return                     true: 调用成功，等待 {@link IZegoCreateConversationCallback#onCreateConversation(int, String, String)} 返回；false: 调用失败
//     *
//     * @see #setRoomConfig(boolean, boolean)
//     */
//    public boolean createConversation(String conversationName, MiguUser[] listMember, IMiguCreateConversationCallback callback){
//        return g_ZegoApi.createConversation(conversationName,listMember,callback);
//    }
//
//    /**
//     * 在会话中发送一条消息。
//     * 消息发送成功后，参与会话的成员列表会通过 {@link IZegoIMCallback#onRecvConversationMessage(String, String, ZegoConversationMessage)} 收到此消息
//     * 注意：发送会话消息前，必须调用 {@link #createConversation(String, ZegoUser[], IZegoCreateConversationCallback)} 成功创建会话
//     *
//     * @param messageType       消息类型， 详见 {@link com.migu.miguimsdk.constants.ZegoIM.MessageType}
//     * @param conversationID    会话 ID，由 server 下发。在 {@link #createConversation(String, ZegoUser[], IZegoCreateConversationCallback)} 的回调 {@link IZegoCreateConversationCallback#onCreateConversation(int, String, String)} 中获得
//     * @param content           消息内容，长度 <= 1024 bytes 的可打印字符串
//     * @param callback          实现 {@link IZegoConversationMessageCallback} 接口的对象实例，用于接收发送消息结果及 server 下发的 messageID
//     * @return                  true:调用成功，等待 {@link IZegoConversationMessageCallback#onSendConversationMessage(int, String, String, long)} 回调；false:调用失败
//     *
//     * @see IZegoIMCallback#onRecvConversationMessage(String, String, ZegoConversationMessage)
//     */
//    public boolean sendConversationMessage(int messageType, String conversationID, String content, IMiguConversationMessageCallback callback){
//        return g_ZegoApi.sendConversationMessage(messageType,conversationID,content,callback);
//    }
//
//    /**
//     * 收到会话消息。
//     * 当会话成员列表中的任一成员通过 {@link com.migu.miguimsdk.ZegoLiveRoom#sendConversationMessage(int, String, String, IZegoConversationMessageCallback)} 发送一条会话消息后，会话成员列表中的其他成员会通过此回调接收到消息
//     *
//     * @param roomID            房间 ID
//     * @param conversationID    会话 ID
//     * @param message           会话消息, 包含消息内容，消息类型，发送者，发送时间等信息
//     *
//     * @see com.migu.miguimsdk.ZegoLiveRoom#sendConversationMessage(int, String, String, IZegoConversationMessageCallback)
//     * @see com.migu.miguimsdk.ZegoLiveRoom#createConversation(String, ZegoUser[], IZegoCreateConversationCallback)
//     * @see com.migu.miguimsdk.ZegoLiveRoom#setRoomConfig(boolean, boolean)
//     */
//    void onRecvConversationMessage(String roomID, String conversationID, MiguConversationMessage message){
//        g_ZegoApi.onRecvConversationMessage(roomID,conversationID,message);
//    }
//
//    /**
//     * 获取指定会话 ID 的会话信息详情。
//     *
//     * @param conversationID    会话 ID
//     * @param callback          实现了 {@link IZegoGetConversationInfoCallback} 接口的对象实例，用于接收查询到的会话信息详情
//     * @return                  true: 调用成功，等待 {@link IZegoGetConversationInfoCallback#onGetConversationInfo(int, String, String, ZegoConversationInfo)} 回调；false: 调用失败
//     */
//    public boolean getConversationInfo(String conversationID, IMiguGetConversationInfoCallback callback){
//        return g_ZegoApi.getConversationInfo(conversationID,callback);
//    }
//
//
//    /**
//     * 获取指定会话 ID 的会话信息详情。
//     *
//     * @param conversationID    会话 ID
//     * @param callback          实现了 {@link IZegoGetConversationInfoCallback} 接口的对象实例，用于接收查询到的会话信息详情
//     * @return                  true: 调用成功，等待 {@link IZegoGetConversationInfoCallback#onGetConversationInfo(int, String, String, ZegoConversationInfo)} 回调；false: 调用失败
//     */
//    public boolean getConversationInfo(String conversationID, IZegoGetConversationInfoCallback callback){
//        return g_ZegoApi.getConversationInfo(conversationID,callback);
//    }
//
//    /**
//     * 房间成员更新回调。
//     * 仅在登录前通过 {@link com.migu.miguimsdk.ZegoLiveRoom#setRoomConfig(boolean, boolean)} 设置 userStateUpdate 为 true 后，房间内其他成员才能在发生成员增减时收到此回调
//     *
//     * @param listUser     成员更新列表
//     * @param updateType   参考 {@link com.migu.miguimsdk.constants.ZegoIM.UserUpdateType}
//     *
//     * @see com.migu.miguimsdk.ZegoLiveRoom#setRoomConfig
//     */
//    void onUserUpdate(ZegoUserState[] listUser, int updateType){
//        g_ZegoApi.onUserUpdate(listUser,updateType);
//    }


    public static boolean requireHardwareEncoder(boolean require) {
        return ZegoLiveRoom.requireHardwareEncoder(require);
    }

    public static boolean requireHardwareDecoder(boolean require) {
        return ZegoLiveRoom.requireHardwareDecoder(require);
    }

    public static boolean setPlayQualityMonitorCycle(long timeInMS) {
        return ZegoLiveRoom.setPlayQualityMonitorCycle(timeInMS);
    }

    public static void enableExternalRender(boolean enable) {
        ZegoLiveRoom.enableExternalRender(enable);
    }

    public static boolean setVideoCaptureFactory(ZegoVideoCaptureFactory factory) {
        return ZegoLiveRoom.setVideoCaptureFactory(factory, 0);
    }

    public static boolean setVideoCaptureFactory(ZegoVideoCaptureFactory factory, int channelIndex) {
        return ZegoLiveRoom.setVideoCaptureFactory(factory, channelIndex);
    }

    public static boolean setVideoFilterFactory(ZegoVideoFilterFactory factory) {
        return ZegoLiveRoom.setVideoFilterFactory(factory, 0);
    }

    public static boolean setVideoFilterFactory(ZegoVideoFilterFactory factory, int channelIndex) {
        return ZegoLiveRoom.setVideoFilterFactory(factory, channelIndex);
    }

    /**
     * 暂停设备模块。
     * 用于需要暂停指定模块的场合，例如来电时暂定音频模块；
     * 暂停指定模块后，注意在合适时机下恢复模块
     *
     * @param moduleType  设备类型, 参考 {@link ZegoConstants.ModuleType} 定义
     *
     * @see #resumeModule(int)
     */
    public void pauseModule(int moduleType){
        g_ZegoApi.pauseModule(moduleType);
    }

    /**
     * 恢复设备模块。
     * 用于需要恢复指定模块的场合，例如来电结束后恢复音频模块。
     *
     * @param moduleType  设备类型, 参考 {@link ZegoConstants.ModuleType} 定义
     *
     * @see #pauseModule(int)
     */
    public void resumeModule(int moduleType){
        g_ZegoApi.pauseModule(moduleType);
    }

    /**
     * 上报日志
     *
     * @discussion 上传日志到后台便于分析问题
     */
    public static void uploadLog() {
        ZegoLiveRoom.uploadLog();
    }

    /**
     * 消息分类.
     */
    final public static class MessageCategory{

        /**
         * 聊天.
         */
        public static final int Chat = 1;

        /**
         * 系统.
         */
        public static final int System = 2;

        /**
         * 点赞.
         */
        public static final int Like = 3;

        /**
         * 礼物.
         */
        public static final int Gift = 4;

        /**
         * 其它.
         */
        public static final int OtherCategory = 100;
    }

    private MiguStreamInfo zegoStreamInfoToMigu(ZegoStreamInfo zegoStreamInfo) {
        MiguStreamInfo miguStreamInfo = new MiguStreamInfo();
        miguStreamInfo.extraInfo = zegoStreamInfo.extraInfo;
        miguStreamInfo.streamID = zegoStreamInfo.streamID;
        miguStreamInfo.userID = zegoStreamInfo.userID;
        miguStreamInfo.userName = zegoStreamInfo.userName;
        return miguStreamInfo;
    }

}
