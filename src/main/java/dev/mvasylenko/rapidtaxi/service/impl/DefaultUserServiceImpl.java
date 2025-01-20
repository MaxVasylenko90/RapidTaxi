package dev.mvasylenko.rapidtaxi.service.impl;

import dev.mvasylenko.rapidtaxi.entity.User;
import dev.mvasylenko.rapidtaxi.repository.UserRepository;
import dev.mvasylenko.rapidtaxi.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("defaultUserServiceImpl")
public class DefaultUserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public DefaultUserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByName(username);
    }
}
