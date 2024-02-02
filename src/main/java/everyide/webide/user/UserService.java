package everyide.webide.user;

import everyide.webide.config.auth.dto.request.SignRequestDto;
import everyide.webide.user.domain.User;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    public void clearRefreshToken(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
        user.setRefreshToken(null); // 리프레시 토큰 값 제거
        userRepository.save(user);
    }

    public void signUpUser(SignRequestDto signRequestDto) {
        User user = User.builder()
                .name(signRequestDto.getName())
                .email(signRequestDto.getUsername())
                .password(passwordEncoder.encode(signRequestDto.getPassword()))
                .role("USER")
                .build();
        userRepository.save(user);
    }
}
