package moa.moabackend.global.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import moa.moabackend.domain.user.Role;
import moa.moabackend.global.security.JwtTokenProvider;
import moa.moabackend.global.security.TokenResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = getEmail(oAuth2User);

        TokenResponse tokenResponse = jwtTokenProvider.createToken(email, Role.USER);

        String targetUrl = UriComponentsBuilder.fromUriString("/login/success")
                .queryParam("accessToken", tokenResponse.getAccessToken())
                .queryParam("refreshToken", tokenResponse.getRefreshToken())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String getEmail(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        if (attributes.containsKey("email")) {
            return (String) attributes.get("email");
        }
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount != null) {
            return (String) kakaoAccount.get("email");
        }
        return null;
    }
}
