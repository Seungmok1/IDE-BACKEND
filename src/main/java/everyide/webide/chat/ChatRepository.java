package everyide.webide.chat;

import everyide.webide.chat.domain.Chat;
import everyide.webide.container.domain.Container;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Chat findByContainerId(Long containerId);
}
