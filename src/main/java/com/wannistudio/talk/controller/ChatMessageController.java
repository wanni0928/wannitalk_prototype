package com.wannistudio.talk.controller;

import com.wannistudio.talk.domain.Member;
import com.wannistudio.talk.service.ChatMessageService;
import com.wannistudio.talk.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {
    @Autowired
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;
    private final MemberService memberService;

//    @MessageMapping("/chat/send")
//    public void sendMsg(ChatMessageForm form) {
//        ChatMessage chatMessage = new ChatMessage();
//        chatMessage.setMember(memberService.findOne(11L));
//        chatMessage.setMessage(form.getMessage());
//        chatMessage.setTime(LocalDateTime.now());
//        chatMessageService.sendMsg(chatMessage);
//        simpMessagingTemplate.convertAndSend("/topic/" + form.getName(), form);
//    }
//
    @GetMapping("/chat")
    public String chatHome(Model model) {
        Member member = memberService.findOne(11L);
        model.addAttribute("member", member);
        return "chat/main";
    }
}
