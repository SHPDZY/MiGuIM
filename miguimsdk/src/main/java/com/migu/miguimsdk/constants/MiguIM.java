package com.migu.miguimsdk.constants;

/**
 * Copyright © 2018 Migu. All rights reserved.
 *
 * @author jone on 2018/1/23..
 */
public class MiguIM {
    public static final class ChatRoomConnectState {
        public static final int Disconnected = 1;
        public static final int WaitingConnect = 2;
        public static final int Connecting = 3;
        public static final int Connected = 4;

        public ChatRoomConnectState() {
        }
    }

    public static final class MessagePriority {
        public static final int Default = 2;
        public static final int High = 3;

        public MessagePriority() {
        }
    }

    public static final class MessageCategory {
        public static final int Chat = 1;
        public static final int System = 2;
        public static final int Like = 3;
        public static final int Gift = 4;
        public static final int OtherCategory = 100;

        public MessageCategory() {
        }
    }

    public static final class MessageType {
        public static final int Text = 1;
        public static final int Picture = 2;
        public static final int File = 3;
        public static final int OtherType = 100;

        public MessageType() {
        }
    }

    public static final class UserUpdateFlag {
        public static final int ADDED = 1;
        public static final int DELETED = 2;

        public UserUpdateFlag() {
        }
    }

    public static final class UserUpdateType {
        public static final int TOTAL = 1;
        public static final int INCREASE = 2;

        public UserUpdateType() {
        }
    }
}
