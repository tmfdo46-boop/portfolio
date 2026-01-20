package com.portfolio.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.portfolio.model.User;
import com.portfolio.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 생성자
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if(userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            return userOpt.get();
        }
        return null;
    }
    
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 유저가 존재하지 않습니다. id=" + id));
    }
    
    public Map<String, Integer> userCounts(Long userId) {
        int followerCount = userRepository.countFollowersByUserId(userId);
        int followingCount = userRepository.countFollowingByUserId(userId);
        int postCount = userRepository.countPostsByUserId(userId);

        Map<String, Integer> map = new HashMap<>();
        map.put("followerCount", followerCount);
        map.put("followingCount", followingCount);
        map.put("postCount", postCount);

        return map;
    }

    @Transactional
    public void updateProfile(Long userId, String nickname, String hp, String address, String bio) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        user.setNickname(nickname);
        user.setHp(hp);
        user.setAddress(address);
        user.setBio(bio);
    }
}
