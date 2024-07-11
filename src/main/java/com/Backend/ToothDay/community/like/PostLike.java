package com.Backend.ToothDay.community.like;

import com.Backend.ToothDay.community.post.Post;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter @Setter
@Entity
public class PostLike {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likeId")
    private int likeId;

    @JoinColumn(name = "postId")
    @ManyToOne
    private Post post;

    //user
}
