package everyide.webide.chat;

import everyide.webide.chat.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {

    List<Message> findTop30ByContainerIdOrderBySendDateDesc(String containerId);
}
