package com.wannistudio.talk.controller;

import com.wannistudio.talk.domain.test.HelloMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GreetController {
    @Autowired
    private final SimpMessageSendingOperations messageTemplate;

//    @MessageMapping("/hello")
////    @SendTo("/topic/greetings")
//
//    public void greeting(HelloMessage message) throws Exception {
//        Thread.sleep(1000); // simulated delay
//        System.out.println(message);
////        ObjectMapper mapper = new ObjectMapper();
//
//        Greeting greeting = new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
//
////        String jsonString = mapper.writeValueAsString(message);
////        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
//        messageTemplate.convertAndSend("/topic/greetings", greeting);
//    }
    @MessageMapping("/hello")
    public void greeting(HelloMessage helloMessage) throws Exception {
        messageTemplate.convertAndSend("/topic/greetings", helloMessage.getName());
    }

    @MessageMapping("/bye")
    public void gret(HelloMessage helloMessage) {
        messageTemplate.convertAndSend("/topic/greetings", helloMessage.getName() + " bye");
    }
}
