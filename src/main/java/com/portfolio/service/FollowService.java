package com.portfolio.service;

import com.portfolio.model.Follow;
import com.portfolio.model.User;
import com.portfolio.repository.FollowRepository;
import com.portfolio.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    // 팔로우하기
    public void follow(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("팔로워 유저 없음"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new RuntimeException("팔로잉 유저 없음"));

        if (!followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            followRepository.save(new Follow(follower, following));
        }
    }
    
    // 내가 팔로우 중인 유저 목록 반환.
    public List<User> getFollowingUsers(Long followerId) {
        return followRepository.findByFollowerId(followerId)
                               .stream()
                               .map(f -> f.getFollowing())
                               .filter(Objects::nonNull)
                               .collect(Collectors.toList());
    }

    // 팔로우 여부 체크
    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }
}
