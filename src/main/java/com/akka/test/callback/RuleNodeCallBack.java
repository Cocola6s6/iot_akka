package com.akka.test.callback;

import com.akka.test.message.KeyValue;
import com.akka.test.exception.RuleEngineException;

import java.util.List;

public interface RuleNodeCallBack extends CallBack {

    void onSuccess(List<KeyValue> params);

    void onFailure(RuleEngineException e);
}
