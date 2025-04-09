package dev.mvasylenko.rapidtaxi.service.impl;

import dev.mvasylenko.rapidtaxi.model.Role;
import dev.mvasylenko.rapidtaxi.model.User;
import dev.mvasylenko.rapidtaxi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

import static dev.mvasylenko.rapidtaxi.constants.Constants.*;

@Service("oauth2UserServiceImpl")
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
    private static final String NAME = "name";
    private static final String ROLE = "ROLE_";
    private static final String DEFAULT_PASSWORD_FOR_OAUTH2_USER = "defaultPassword";
    private static final String DEFAULT_PHONE_NUMBER_FOR_OAUTH2_USER = "123456789";

    private final UserRepository userRepository;

    @Autowired
    public OAuth2UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String email = oauth2User.getAttribute(EMAIL);

        var userFromDb = userRepository.findByEmail(email);
        User requestedUser = userFromDb.orElseGet(() -> createNewOauthUserInDb(email, oauth2User.getAttribute(NAME)));

        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority(ROLE + requestedUser.getRole().name())),
                oauth2User.getAttributes(), EMAIL);
    }

    private User createNewOauthUserInDb(String email, String name) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(DEFAULT_PASSWORD_FOR_OAUTH2_USER);
        user.setPhoneNumber(DEFAULT_PHONE_NUMBER_FOR_OAUTH2_USER);
        user.setRole(Role.GUEST);
        userRepository.save(user);
        return user;
    }
}
