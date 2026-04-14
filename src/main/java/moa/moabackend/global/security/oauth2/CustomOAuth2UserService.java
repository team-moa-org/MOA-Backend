package moa.moabackend.global.security.oauth2;

import lombok.RequiredArgsConstructor;
import moa.moabackend.domain.user.Provider;
import moa.moabackend.domain.user.Role;
import moa.moabackend.domain.user.User;
import moa.moabackend.domain.user.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Provider provider = Provider.valueOf(registrationId.toUpperCase());
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuth2UserInfo oAuth2UserInfo = getOAuth2UserInfo(registrationId, attributes);

        User user = saveOrUpdate(oAuth2UserInfo, provider);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())),
                attributes,
                userNameAttributeName
        );
    }

    private OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase("google")) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase("kakao")) {
            return new KakaoOAuth2UserInfo(attributes);
        }
        throw new IllegalArgumentException("지원하지 않는 로그인입니다.");
    }

    private User saveOrUpdate(OAuth2UserInfo oAuth2UserInfo, Provider provider) {
        User user = userRepository.findByProviderAndProviderId(provider, oAuth2UserInfo.getId())
                .map(entity -> {
                    entity.updateName(oAuth2UserInfo.getName());
                    return entity;
                })
                .orElse(User.builder()
                        .email(oAuth2UserInfo.getEmail())
                        .name(oAuth2UserInfo.getName())
                        .role(Role.USER)
                        .provider(provider)
                        .providerId(oAuth2UserInfo.getId())
                        .build());

        return userRepository.save(user);
    }
}
