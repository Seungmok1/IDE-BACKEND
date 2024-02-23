package everyide.webide.chat;

import everyide.webide.chat.domain.Chat;
import everyide.webide.container.domain.Container;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public void createChat(Long containerId) {
        chatRepository.save(new Chat(containerId));
    }
}
