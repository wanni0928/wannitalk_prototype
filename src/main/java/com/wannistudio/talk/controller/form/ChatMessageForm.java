package com.wannistudio.talk.controller.form;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChatMessageForm {
    private String name;
    private String sender;
    private String message;
}
