package com.Backend.ToothDay.jwt.service;

import com.Backend.ToothDay.community.comment.CommentRepository;
import com.Backend.ToothDay.community.image.ImageRepository;
import com.Backend.ToothDay.community.post.PostRepository;
import com.Backend.ToothDay.jwt.config.jwt.JwtUtil;
import com.Backend.ToothDay.jwt.model.User;
import com.Backend.ToothDay.jwt.repository.UserRepository;
import com.Backend.ToothDay.visit.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public void deleteUser(Long userId) {
        // 사용자 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        // 사용자와 연결된 포스트 모두 삭제
        postRepository.deleteByUser(user);

        // 사용자 삭제
        userRepository.delete(user);
    }
}

