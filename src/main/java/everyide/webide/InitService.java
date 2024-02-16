//package everyide.webide;
//
// import everyide.webide.config.auth.dto.request.SignRequestDto;
// import everyide.webide.container.domain.Container;
// import everyide.webide.room.domain.Room;
// import everyide.webide.user.UserService;
// import everyide.webide.user.domain.User;
// import jakarta.persistence.EntityManager;
// import lombok.RequiredArgsConstructor;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
//
// @Service
// @Transactional
// @RequiredArgsConstructor
//public class InitService {
//
//     private final EntityManager em;
//     private final BCryptPasswordEncoder bCryptPasswordEncoder;
//     private final UserService userService;
//
//     public void dbInit() {
//
//         SignRequestDto signRequestDto = new SignRequestDto("kms@goorm.io", "kms", bCryptPasswordEncoder.encode("1234"), bCryptPasswordEncoder.encode("1234"));
//         userService.signUpUser(signRequestDto);
//
//         User user = User.builder()
//                 .name("hi")
//                 .email("1@1.com")
//                 .password(bCryptPasswordEncoder.encode("12345678"))
//                 .role("USER")
//                 .build();
//
//         Room room = Room.builder()
//                 .name("123")
//                 .build();
//
//         Container container = Container.builder()
//                 .name("123")
//
//                 .build();
//
//
//         em.persist(user);
//         em.persist(room);
//         em.persist(container);
//
//     }
//}
