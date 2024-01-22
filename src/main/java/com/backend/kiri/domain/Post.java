package com.backend.kiri.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Post {
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;
    private boolean isFromSchool;
    private String depart;
    private String arrive;
    private LocalDateTime departTime;
    private LocalDateTime createdTime;
    private Integer cost;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberPost> memberPosts = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoom_id")
    private ChatRoom chatRoom;

    // 연관관계 메서드
    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        chatRoom.setPost(this);
    }

    public void addMember(Member member, boolean isAuthor){
        MemberPost memberPost = new MemberPost(this, member, isAuthor);
        memberPosts.add(memberPost);
        member.getMemberPosts().add(memberPost);
    }

    public void removeMember(Member member){
        for (MemberPost memberPost : memberPosts) {
            if(memberPost.getPost().equals(this) && memberPost.getMember().equals(member)){
                memberPost.getMember().getMemberPosts().remove(memberPost);
                memberPost.setPost(null);
                memberPost.setMember(null);
            }
        }
    }
}