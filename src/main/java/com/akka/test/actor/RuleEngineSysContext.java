package com.akka.test.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.akka.test.callback.SimpleServiceCallback;
import com.akka.test.message.KeyValue;
import com.akka.test.message.Metadata;
import com.akka.test.message.ToRuleMsg;
import com.akka.test.message.TriggerInfo;
import com.akka.test.message.enums.TriggerType;
import com.akka.test.service.ServiceContext;
import com.typesafe.config.ConfigFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;


@Service
@Slf4j
public class RuleEngineSysContext {

    @Autowired
    private ServiceContext serviceContext;


    private ActorSystem system;

    private ActorRef appRootActor;

    private static final String appDispatchName = "engine-dispatcher";

    private static final String mailbox = "engine-mailbox";

    @PostConstruct
    public void init() {

        log.info("init actor system start");
        system = ActorSystem.create("sys", ConfigFactory.parseResources("actor-system.conf").withFallback(ConfigFactory.load()));

        appRootActor = system.actorOf(Props.create(AppRootActor.class, 1l, "appRootActor", serviceContext).withMailbox(mailbox).withDispatcher(appDispatchName), "iot_akka");

        log.info("init actor system end");

    }


    public void tellRuleNode(List<KeyValue> keyValues, Metadata metadata, SimpleServiceCallback<Void> callback) {

        appRootActor.tell(ToRuleMsg.builder().keyValues(keyValues).metadata(metadata).triggerInfo(TriggerInfo.builder().type(TriggerType.MESSAGE).build()).callback(callback).build(), ActorRef.noSender());

    }
}
