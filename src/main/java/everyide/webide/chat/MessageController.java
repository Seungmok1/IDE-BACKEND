package everyide.webide.chat;

import everyide.webide.chat.domain.Chat;
import everyide.webide.chat.domain.Message;
import everyide.webide.chat.domain.dto.ManyMessagesResponseDto;
import everyide.webide.chat.domain.dto.MessageRequestDto;
import everyide.webide.chat.domain.dto.MessageResponseDto;
import everyide.webide.user.UserRepository;
import everyide.webide.user.domain.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

@Service
@Controller
@RequiredArgsConstructor
public class MessageController {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    @MessageMapping("/api/container/{containerId}/chat")
    @SendTo("/api/topic/container/{containerId}/chat")
    public MessageResponseDto message(MessageRequestDto messageRequestDto, @DestinationVariable String containerId) {
        User user = userRepository.findById(messageRequestDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("없는 유저"));

        Message message = Message.builder()
                .containerId(containerId)
                .content(messageRequestDto.getContent())
                .userId(messageRequestDto.getUserId())
                .userName(user.getName())
                .build();
        messageRepository.save(message);

        Chat chat = chatRepository.findByContainerId(Long.valueOf(containerId));
        chat.addMessage(message);

        return new MessageResponseDto(message.getId(), message.getUserId(), user.getName(), messageRequestDto.getContent());
    }
}
