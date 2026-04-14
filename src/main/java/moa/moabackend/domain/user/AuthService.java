package moa.moabackend.domain.user;

import lombok.RequiredArgsConstructor;
import moa.moabackend.global.error.exception.CustomException;
import moa.moabackend.global.error.exception.ErrorCode;
import moa.moabackend.global.security.JwtTokenProvider;
import moa.moabackend.global.security.TokenResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signup(UserDto.SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS) {};
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(Role.USER)
                .provider(Provider.LOCAL)
                .build();

        userRepository.save(user);
    }

    public TokenResponse login(UserDto.LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND) {});

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED) {};
        }

        return jwtTokenProvider.createToken(user.getEmail(), user.getRole());
    }

    @Transactional
    public TokenResponse reissue(UserDto.ReissueRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED) {};
        }

        String email = jwtTokenProvider.getEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND) {});

        return jwtTokenProvider.createToken(user.getEmail(), user.getRole());
    }
}
