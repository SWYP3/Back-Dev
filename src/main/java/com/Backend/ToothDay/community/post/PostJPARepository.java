package com.Backend.ToothDay.community.post;

import com.Backend.ToothDay.community.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostJPARepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContaining(String keyword);
}
