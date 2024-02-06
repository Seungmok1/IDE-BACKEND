package everyide.webide.user;

import everyide.webide.config.auth.dto.request.SignRequestDto;
import everyide.webide.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void signUpUser(SignRequestDto signRequestDto) {
        User user = User.builder()
                .name(signRequestDto.getName())
                .email(signRequestDto.getEmail())
                .password(passwordEncoder.encode(signRequestDto.getPassword()))
                .role("USER")
                .build();
        userRepository.save(user);
    }

    public boolean changePassword(String email, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false; // 기존 비밀번호와 oldPassword가 일치하지 않음
        }
        user.setPassword(passwordEncoder.encode(newPassword)); // 비밀번호 업데이트
        userRepository.save(user);
        return true; // 비밀번호 변경 성공
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

}
