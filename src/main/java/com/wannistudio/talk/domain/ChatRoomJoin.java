package com.wannistudio.talk.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class ChatRoomJoin {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name =  "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;
}
