package everyide.webide.room.domain.dto;

import everyide.webide.room.domain.Room;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class EnterRoomResponseDto {
    private Room room;
    private Long ownerId;
    private List<String> usersName;

    @Builder
    public EnterRoomResponseDto(Room room, Long ownerId, List<String> usersName) {
        this.room = room;
        this.ownerId = ownerId;
        this.usersName = usersName;
    }
}
