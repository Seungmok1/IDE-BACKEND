package everyide.webide.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketUserCountMapper {

    private final ConcurrentHashMap<Long, Long> userCountMap = new ConcurrentHashMap<>();

    public void increase(Long containerId) {
        userCountMap.compute(containerId, (key, value) -> (value == null) ? 1L : value + 1);
    }

    public void decrease(Long containerId) {
        userCountMap.computeIfPresent(containerId, (key, value) -> value - 1);
        if (userCountMap.get(containerId) == 0) {
            userCountMap.remove(containerId);
        }
    }

    public Long get(Long containerId) {
        return userCountMap.get(containerId);
    }
}
