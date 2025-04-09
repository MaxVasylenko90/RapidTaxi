package dev.mvasylenko.rapidtaxi.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mvasylenko.rapidtaxi.model.User;
import dev.mvasylenko.rapidtaxi.repository.UserRepository;
import dev.mvasylenko.rapidtaxi.security.jwt.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

import static dev.mvasylenko.rapidtaxi.constants.Constants.*;

@Component("oAuth2AuthenticationSuccessHandler")
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final String APPLICATION_JSON = "application/json";
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public OAuth2AuthenticationSuccessHandler(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        final OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute(EMAIL);
        User user = userRepository.findByEmail(email).orElseThrow();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);


        response.setContentType(APPLICATION_JSON);
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(Map.of(ACCESS_TOKEN, accessToken, REFRESH_TOKEN, refreshToken)));
    }
}
