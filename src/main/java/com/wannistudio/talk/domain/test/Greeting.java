package com.wannistudio.talk.domain.test;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class Greeting {
    private String content;

    public Greeting(String content) {
        this.content = content;
    }
}
