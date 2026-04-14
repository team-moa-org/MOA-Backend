package moa.moabackend.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class UserDto {

    @Getter
    @Setter
    public static class SignupRequest {
        @NotBlank @Email
        private String email;
        @NotBlank
        private String password;
        @NotBlank
        private String name;
    }

    @Getter
    @Setter
    public static class LoginRequest {
        @NotBlank @Email
        private String email;
        @NotBlank
        private String password;
    }

    @Getter
    @Setter
    public static class ReissueRequest {
        @NotBlank
        private String refreshToken;
    }
}
