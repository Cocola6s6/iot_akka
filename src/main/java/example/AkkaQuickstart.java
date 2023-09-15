package example;

import java.io.IOException;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class AkkaQuickstart {
    public static void main(String[] args) {
//         Actors 容器
        final ActorSystem system = ActorSystem.create("hello_akka");
        try {
//            printerActor
            final ActorRef printerActor = system.actorOf(Printer.props(), "printerActor");
//            howdyActor
            final ActorRef howdyGreeter = system.actorOf(Greeter.props("Howdy", printerActor), "howdyGreeter");
//            helloActor
            final ActorRef helloGreeter = system.actorOf(Greeter.props("Hello", printerActor), "helloGreeter");
//            goodDayActor
            final ActorRef goodDayGreeter = system.actorOf(Greeter.props("Good day", printerActor), "goodDayGreeter");

//            发送消息给howdyActor的邮箱
//            分为两种消息：WhoToGreet和Greet，前者将更新 Actor 的问候语状态，后者将触发向Printer Actor发送问候语。
            howdyGreeter.tell(new Greeter.WhoToGreet("Akka"), ActorRef.noSender());
            howdyGreeter.tell(new Greeter.Greet(), ActorRef.noSender());
//
//            howdyGreeter.tell(new example.Greeter.WhoToGreet("Lightbend"), ActorRef.noSender());
//            howdyGreeter.tell(new example.Greeter.Greet(), ActorRef.noSender());
//
//            helloGreeter.tell(new example.Greeter.WhoToGreet("Java"), ActorRef.noSender());
//            helloGreeter.tell(new example.Greeter.Greet(), ActorRef.noSender());
//
//            goodDayGreeter.tell(new example.Greeter.WhoToGreet("Play"), ActorRef.noSender());
//            goodDayGreeter.tell(new example.Greeter.Greet(), ActorRef.noSender());

            System.out.println(">>> Press ENTER to exit <<<");
            System.in.read();
        } catch (IOException ioe) {
        } finally {
            system.terminate();
        }
    }
}