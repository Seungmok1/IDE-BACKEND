package everyide.webide.websocket;

import everyide.webide.room.RoomRepository;
import everyide.webide.room.domain.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketRoomUserCountMapper {

    private final ConcurrentHashMap<String, Integer> roomUserCountMap = new ConcurrentHashMap<>();
    private final RoomRepository roomRepository;

    public boolean increase(String containerId) {
        if (isMaxPeople(containerId)) {
            log.warn("더 이상 공간이 없습니다.");
            return false;
        }
        roomUserCountMap.compute(containerId, (key, value) -> (value == null) ? 1 : value + 1);
        return true;
    }


    public void decrease(String containerId) {
        roomUserCountMap.computeIfPresent(containerId, (key, value) -> value - 1);
        if (roomUserCountMap.get(containerId) == 0) {
            roomUserCountMap.remove(containerId);
        }
    }

    public int get(String containerId) {
        return roomUserCountMap.get(containerId);
    }

    private boolean isMaxPeople(String containerId) {
        Room room = roomRepository.findRoomByContainerId(containerId);
        return roomUserCountMap.get(containerId).equals(room.getMaxPeople());
    }
}