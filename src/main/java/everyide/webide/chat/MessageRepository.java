package everyide.webide.chat;

import everyide.webide.chat.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {

    List<Message> findTop10ByContainerIdOrderBySendDateDesc(String containerId);

    List<Message> findTop10ByContainerIdAndIdLessThanOrderBySendDateDesc(String containerId, String id);

    List<Message> findTop10ByContainerIdAndIdGreaterThanOrderBySendDateAsc(String containerId, String id);

}
