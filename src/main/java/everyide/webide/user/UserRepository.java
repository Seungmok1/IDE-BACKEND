package everyide.webide.user;

import everyide.webide.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findAllByRefreshToken(String refreshToken);
    Optional<User> findByEmail(String email);


}

