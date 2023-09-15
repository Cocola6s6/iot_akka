package com.akka.test.message;

import com.akka.test.message.enums.RuleEngineMsgType;
import com.akka.test.callback.CallBack;

/**
 * Actor之间的消息载体，包括了异步回调、消息类型。
 */
public interface ActorMsg {

    RuleEngineMsgType getMsgType();

    CallBack getCallBack();
}
