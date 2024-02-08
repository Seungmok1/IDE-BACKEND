package everyide.webide.websocket.containerloading;

import everyide.webide.websocket.containerloading.domain.ContainerLoadingDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ContainerLoadingController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public void broadcastContainerLoading(String roomId, ContainerLoadingDto dto) {
        log.trace("웹소켓 [컨테이너] [로딩 상태] 전송, projectId = {}, object ={}", roomId, dto.toString());
        simpMessagingTemplate.convertAndSend("/topic/room/" + roomId + "/container-loading", dto);
    }
}
