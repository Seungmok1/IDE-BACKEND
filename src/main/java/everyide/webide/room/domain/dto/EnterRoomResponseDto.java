package everyide.webide.room.domain.dto;

import everyide.webide.room.domain.Room;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class EnterRoomResponseDto {
    private Room room;
    private String ownerName;
    private List<String> usersName;

    @Builder
    public EnterRoomResponseDto(Room room, String ownerName, List<String> usersName) {
        this.room = room;
        this.ownerName = ownerName;
        this.usersName = usersName;
    }
}
