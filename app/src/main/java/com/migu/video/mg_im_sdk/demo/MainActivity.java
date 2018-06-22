package com.migu.video.mg_im_sdk.demo;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.migu.miguimsdk.MiguIMSDK;
import com.migu.miguimsdk.callback.IMiguBigRoomMessageCallback;
import com.migu.miguimsdk.callback.IMiguIMCallback;
import com.migu.miguimsdk.callback.IMiguLoginCompletionCallback;
import com.migu.miguimsdk.callback.IMiguRoomCallback;
import com.migu.miguimsdk.constants.MiguIM;
import com.migu.miguimsdk.entity.MiguBigRoomMessage;
import com.migu.miguimsdk.entity.MiguConversationMessage;
import com.migu.miguimsdk.entity.MiguRoomMessage;
import com.migu.miguimsdk.entity.MiguStreamInfo;
import com.migu.miguimsdk.entity.MiguUserState;
import com.migu.miguimsdk.utils.MiguIMLogUtil;
import com.migu.video.mg_im_sdk.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final private byte[] UDP_SIGN_KEY = new byte[]{
            (byte) 0xf4, (byte) 0x73, (byte) 0x84, (byte) 0x9f, (byte) 0xa3, (byte) 0x98, (byte) 0xae, (byte) 0x96,
            (byte) 0xc8, (byte) 0xc4, (byte) 0xd4, (byte) 0x5d, (byte) 0x00, (byte) 0xc4, (byte) 0x48, (byte) 0x27,
            (byte) 0x09, (byte) 0xb5, (byte) 0x85, (byte) 0x34, (byte) 0x80, (byte) 0xdc, (byte) 0x5b, (byte) 0x75,
            (byte) 0x67, (byte) 0x3d, (byte) 0x92, (byte) 0x44, (byte) 0xb4, (byte) 0x27, (byte) 0x30, (byte) 0x35};
    private long appID = 583682248;


//    static final private byte[] UDP_SIGN_KEY = new byte[] { (byte)0x1e, (byte)0xc3, (byte)0xf8, (byte)0x5c, (byte)0xb2, (byte)0xf2, (byte)0x13, (byte)0x70,
//            (byte)0x26, (byte)0x4e, (byte)0xb3, (byte)0x71, (byte)0xc8, (byte)0xc6, (byte)0x5c, (byte)0xa3,
//            (byte)0x7f, (byte)0xa3, (byte)0x3b, (byte)0x9d, (byte)0xef, (byte)0xef, (byte)0x2a, (byte)0x85,
//            (byte)0xe0, (byte)0xc8, (byte)0x99, (byte)0xae, (byte)0x82, (byte)0xc0, (byte)0xf6, (byte)0xf8 };
//    private long appID = 1739272706L;


    private MiguIMSDK miguIMSDK;

    private TextView tv;


    private String userID;
    private String userName;
    private Button send, init, connect, disconnect;
    private EditText editext, roomid, roomname;
    private String roomID;
    private ScrollView scrollView;
    private boolean isInit, isConnect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        tv = (TextView) findViewById(R.id.sample_text);
        send = (Button) findViewById(R.id.send);
        connect = (Button) findViewById(R.id.connect);
        disconnect = (Button) findViewById(R.id.disconnect);
        init = (Button) findViewById(R.id.init);
        editext = (EditText) findViewById(R.id.editext);
        roomid = (EditText) findViewById(R.id.roomid);
        roomname = (EditText) findViewById(R.id.roomname);
        final long ms = System.currentTimeMillis();
        userID = ms + "";
        userName = "Android_" + getOsInfo() + "-" + ms;
        MiguIMSDK.setTestEnv(false);
        MiguIMSDK.setVerbose();
        miguIMSDK = new MiguIMSDK();


        init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInit) {
                    return;
                }

                boolean b = miguIMSDK.initSDK(appID, UDP_SIGN_KEY, getBaseContext());
                if (!b) {
                    Toast.makeText(getBaseContext(), "SDK初始化失败!", Toast.LENGTH_SHORT).show();
                    setMessage("SDK初始化失败");
                } else {
                    isInit = true;
                    Toast.makeText(getBaseContext(), "SDK初始化成功!", Toast.LENGTH_SHORT).show();
                    setMessage("SDK初始化成功");
                    setMiguIMListener();
                }
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnect) {
                    return;
                }
                if (!isInit) {
                    Toast.makeText(getBaseContext(), "SDK未初始化", Toast.LENGTH_SHORT).show();
                    return;
                }
                MiguIMSDK.setUser(userID, userName);
                boolean loginRoom = miguIMSDK.connection(roomid.getText().toString(), roomname.getText().toString(), new IMiguLoginCompletionCallback() {

                    @Override
                    public void onLoginCompletion(int i, MiguStreamInfo[] miguStreamInfos) {
                        Log.e("MGIMSDK","onLoginCompletion code:" + i + ",miguStreamInfos:" + miguStreamInfos.toString());
                        if (i == 0) {
                            isConnect = true;
                            roomid.setEnabled(false);
                            roomid.setFocusable(false);
                            roomname.setEnabled(false);
                            roomname.setFocusable(false);
                            setMessage("创建长连接成功");
                        } else {
                            isConnect = false;
                            setMessage("创建长连接失败 errorcode:" + i);
                        }
                    }
                });
                if (!loginRoom) {
                    Toast.makeText(getBaseContext(), "加入房间失败!", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getBaseContext(), "加入房间成功!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isConnect) {
                    return;
                }
                if (!isInit) {
                    Toast.makeText(getBaseContext(), "SDK未初始化", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isConnect) {
                    Toast.makeText(getBaseContext(), "未创建长连接", Toast.LENGTH_SHORT).show();
                    return;
                }
                isConnect = false;
                boolean b = miguIMSDK.disConnection();
                if (b) {
                    roomid.setEnabled(true);
                    roomid.setFocusable(true);
                    roomname.setEnabled(true);
                    roomname.setFocusable(true);
                    setMessage("断开长连接成功");
                } else {
                    setMessage("断开长连接失败");
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isInit) {
                    Toast.makeText(getBaseContext(), "SDK未初始化", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isConnect) {
                    Toast.makeText(getBaseContext(), "未创建长连接", Toast.LENGTH_SHORT).show();
                    return;
                }
                doSendRoomMsg(editext.getText().toString());
            }
        });

    }

    private void setMiguIMListener() {
        // Example of a call to a native method
        miguIMSDK.setMiGuRoomCallback(new IMiguRoomCallback() {
            @Override
            public void onKickOut(int reason, String roomId) {
                Log.e("MGIMSDK","setMiGuRoomCallback onKickOut:" + "被挤出房间, reason: " + reason + "; roomId:" + roomId);
            }

            @Override
            public void onDisconnect(int errorCode, String s) {
                Log.e("MGIMSDK","setMiGuRoomCallback onDisconnect:" + "用户掉线, errorCode: " + errorCode + "; roomId:" + s);

            }

            @Override
            public void onReconnect(int i, String s) {

            }

            @Override
            public void onTempBroken(int i, String s) {

            }

            @Override
            public void onStreamUpdated(int i, MiguStreamInfo[] miguStreamInfos, String s) {

            }

            @Override
            public void onStreamExtraInfoUpdated(MiguStreamInfo[] miguStreamInfos, String s) {

            }

            /**
             * 收到自定义消息.
             *
             * @param fromUserId     消息来源UserID
             * @param fromUserName   消息来源UserName
             * @param content    消息内容
             * @param roomId     房间ID
             */
            @Override
            public void onRecvCustomCommand(String fromUserId, String fromUserName, String content, String roomId) {
                Log.e("MGIMSDK","setMiGuRoomCallback onRecvCustomCommand:" + "收到自定义消息, fromUserId: " + fromUserId + "; fromUserName:" + fromUserName + "; content:" + content);
                setMessage(fromUserId + ":" + content+"(收到自定义消息)");
            }
        });

        miguIMSDK.setMiGuIMCallback(new IMiguIMCallback() {
            @Override
            public void onUserUpdate(MiguUserState[] miguUserStates, int i) {
                Log.e("MGIMSDK","setMiguIMCallback 用户更新 onUserUpdate code:" + i + ",miguUserStates:" + miguUserStates.toString());
            }

            @Override
            public void onRecvRoomMessage(String s, MiguRoomMessage[] miguRoomMessages) {
                Log.e("MGIMSDK","setMiguIMCallback onRecvRoomMessage code:" + s + ",miguRoomMessages:" + miguRoomMessages.toString());
                handleRecvRoomMsg(s, miguRoomMessages);
            }

            @Override
            public void onRecvConversationMessage(String s, String s1, MiguConversationMessage miguConversationMessage) {
                Log.e("MGIMSDK","setMiguIMCallback onRecvConversationMessage s:" + s + ",s1:" + s1 + ",miguConversationMessage:" + miguConversationMessage.toString());

            }

            @Override
            public void onUpdateOnlineCount(String s, int i) {
                Log.e("MGIMSDK","setMiguIMCallback onUpdateOnlineCount code:" + i + ",s:" + s);

            }

            /**
             * 处理房间消息
             * @param s
             * @param miguBigRoomMessages
             */
            @Override
            public void onRecvBigRoomMessage(String s, MiguBigRoomMessage[] miguBigRoomMessages) {
                Log.e("MGIMSDK","setMiguIMCallback onRecvBigRoomMessage s:" + s + ",miguBigRoomMessages:" + miguBigRoomMessages.toString());
                handleRecvBigRoomMsg(s, miguBigRoomMessages);
            }

        });
    }

    /**
     *
     * @param msg
     */
    private void setMessage(String msg) {
        String text = tv.getText().toString() + "";
        tv.setText(text + "\n" + msg);
        scrollView.smoothScrollTo(0, 3000);
    }

    /**
     *
     * @param roomID
     * @param listMsg
     */
    protected void handleRecvRoomMsg(String roomID, MiguRoomMessage[] listMsg) {

        List<MiguRoomMessage> listTextMsg = new ArrayList<>();
        for (MiguRoomMessage message : listMsg) {

            // 文字聊天消息
            if (message.messageType == MiguIM.MessageType.Text && message.messageCategory == MiguIM.MessageCategory.Chat) {
                listTextMsg.add(message);
            }

        }

        if (listTextMsg.size() > 0) {
            for (MiguRoomMessage miguRoomMessage : listTextMsg) {
                setMessage(miguRoomMessage.fromUserID + ":" + miguRoomMessage.content);
            }
        }
    }

    /**
     * 处理房间消息
     * @param roomid 房间号
     * @param miguBigRoomMessages 消息列表
     */
    private void handleRecvBigRoomMsg(String roomid, MiguBigRoomMessage[] miguBigRoomMessages) {
        Log.e("MGIMSDK","handleRecvBigRoomMsg:"+roomid);
        List<MiguBigRoomMessage> listTextMsg = new ArrayList<>();
        for (MiguBigRoomMessage message : miguBigRoomMessages) {

            // 文字聊天消息
            if (message.messageType == MiguIM.MessageType.Text && message.messageCategory == MiguIM.MessageCategory.Chat) {
                listTextMsg.add(message);
            }
        }

        if (listTextMsg.size() > 0) {
            for (MiguBigRoomMessage miguBigRoomMessage : listTextMsg) {
                setMessage(miguBigRoomMessage.fromUserID + ":" + miguBigRoomMessage.content+"(收到普通消息)");
            }
        }
    }


    /**
     * 发送消息
     * @param msg
     */
    protected void doSendRoomMsg(final String msg) {

        MiguRoomMessage roomMessage = new MiguRoomMessage();
        roomMessage.fromUserID = userID;
        roomMessage.fromUserName = userName;
        roomMessage.content = msg;
        roomMessage.messageType = MiguIM.MessageType.Text;
        roomMessage.messageCategory = MiguIM.MessageCategory.Chat;
        roomMessage.messagePriority = MiguIM.MessagePriority.Default;

        miguIMSDK.sendBigRoomMessage(msg, new IMiguBigRoomMessageCallback() {
            @Override
            public void onSendBigRoomMessage(int errorCode, String roomID, String messageID) {
                if (errorCode == 0) {
                    setMessage("我:" + msg + "(发送消息成功)");
                } else {
                    setMessage("我:" + msg + "(发送消息失败)");
                }
            }
        });
    }


    static public String getOsInfo() {

        // 上报系统信息
        StringBuilder oriInfo = new StringBuilder();
        oriInfo.append(Build.MODEL);

        // 替换字符串中的","
        String finalInfo = oriInfo.toString().replaceAll(",", ".");

        return finalInfo;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        miguIMSDK.disConnection();
        releaseSDK();
        miguIMSDK = null;
    }

    /**
     * 用户连续点击两次返回键可以退出应用的时间间隔.
     */
    public static final long EXIT_INTERVAL = 1000;

    private long mBackPressedTime;

    /**
     * 退出.
     */
    private void exit() {
        /* 连按两次退出 */
        long currentTime = System.currentTimeMillis();
        if (currentTime - mBackPressedTime > EXIT_INTERVAL) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mBackPressedTime = currentTime;
        } else {
            // 释放migu sdk
            miguIMSDK.disConnection();
            releaseSDK();
            System.exit(0);
        }
    }

    /**
     * 这个是按下手机返回键的时候  退出提示对话框
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void releaseSDK() {
        // 清空高级设置
        MiguIMSDK.setTestEnv(false);
        MiguIMSDK.enableExternalRender(false);
        // 先置空factory后unintSDK, 或者调换顺序，factory中的destroy方法都会被回调
        MiguIMSDK.setVideoCaptureFactory(null);
        MiguIMSDK.setVideoFilterFactory(null);
        miguIMSDK.unInitSDK();
    }


}
