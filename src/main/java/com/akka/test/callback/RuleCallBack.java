package com.akka.test.callback;

import com.akka.test.exception.RuleEngineException;

public interface RuleCallBack extends CallBack {

    void onSuccess();

    void onFailure(RuleEngineException e);
}