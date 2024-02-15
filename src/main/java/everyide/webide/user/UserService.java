package everyide.webide.user;

import everyide.webide.config.auth.dto.request.SignRequestDto;
import everyide.webide.config.auth.exception.EmailAlreadyUsedException;
import everyide.webide.fileSystem.DirectoryService;
import everyide.webide.fileSystem.domain.Directory;
import everyide.webide.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${file.basePath}")
    private String basePath;

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final DirectoryService directoryService;

    public void signUpUser(SignRequestDto signRequestDto) {
        if (userRepository.findByEmail(signRequestDto.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException("이미 사용 중인 이메일입니다.");
        }

        User user = User.builder()
                .name(signRequestDto.getName())
                .email(signRequestDto.getEmail())
                .password(passwordEncoder.encode(signRequestDto.getPassword()))
                .role("USER")
                .build();
        userRepository.save(user);

        Directory rootDirectory = directoryService.createRootDirectory(user.getEmail());
        if (rootDirectory != null) {
            user.setRootPath(basePath + user.getEmail());
            log.info("회원 등록완료");
        } else {
            log.info("루트 디렉토리 생성불가");
        }

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

    @Transactional(readOnly = true) // 데이터베이스의 데이터를 변경하지 않는 조회 작업에 readOnly = true 옵션을 사용
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

}
