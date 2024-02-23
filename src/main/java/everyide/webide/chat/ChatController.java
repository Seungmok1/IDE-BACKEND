package everyide.webide.chat;

import everyide.webide.chat.domain.Message;
import everyide.webide.chat.domain.dto.ManyMessagesResponseDto;
import everyide.webide.chat.domain.dto.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final MessageRepository messageRepository;

    @GetMapping("/api/container/{containerId}/chat/prev")
    public ResponseEntity<?> getMessages(@PathVariable String containerId, @RequestParam(required = false) String messageId) {

        List<Message> messageList;
        if (messageId != null) {
            messageList = messageRepository.findTop10ByContainerIdAndIdLessThanOrderBySendDateDesc(containerId, messageId);
        } else {
            messageList = messageRepository.findTop10ByContainerIdOrderBySendDateDesc(containerId);
        }

        List<MessageResponseDto> responseDtoList = messageList
                .stream()
                .map(message -> MessageResponseDto.builder()
                        .id(message.getId())
                        .userId(message.getUserId())
                        .name(message.getUserName())
                        .content(message.getContent())
                        .build())
                .collect(Collectors.toList());

        Collections.reverse(responseDtoList);
        return ResponseEntity.ok(new ManyMessagesResponseDto(responseDtoList));
    }

    @GetMapping("/api/container/{containerId}/chat/next")
    public ResponseEntity<?> getNextMessages(@PathVariable String containerId, @RequestParam String messageId) {
        List<MessageResponseDto> messages = messageRepository.findTop10ByContainerIdAndIdGreaterThanOrderBySendDateAsc(containerId, messageId)
                .stream()
                .map(message -> MessageResponseDto.builder()
                        .id(message.getId())
                        .userId(message.getUserId())
                        .name(message.getUserName())
                        .content(message.getContent())
                        .build())
                .collect(Collectors.toList());
        Collections.reverse(messages);
        return ResponseEntity.ok(new ManyMessagesResponseDto(messages));
    }
}
