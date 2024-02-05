package everyide.webide.room;

import everyide.webide.room.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public void create(CreateRoomRequestDto requestDto) {
        Room room = Room.builder()
                .name(requestDto.getName())
                .isLocked(requestDto.getIsLocked())
                .password(requestDto.getPassword())
                .type(RoomType.valueOf(requestDto.getRoomType()))
                .build();
        roomRepository.save(room);
    }

    public List<RoomResponseDto> loadAllRooms() {
        return roomRepository.findAllBy()
                .stream()
                .filter(el -> el.getAvailable())
                .map(el -> RoomResponseDto.builder()
                        .roomId(el.getId())
                        .name(el.getName())
                        .isLocked(el.getIsLocked())
                        .type(el.getType())
                        .available(el.getAvailable())
                        .build())
                .collect(Collectors.toList());
    }
}
