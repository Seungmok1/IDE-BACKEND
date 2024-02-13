package everyide.webide;

import everyide.webide.user.domain.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InitService {
//
//    private final EntityManager em;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    public void dbInit() {
//        User user = User.builder()
//                .name("hi")
//                .email("1@1.com")
//                .password(bCryptPasswordEncoder.encode("12345678"))
//                .role("USER")
//                .build();
//
//        em.persist(user);
//    }
}
