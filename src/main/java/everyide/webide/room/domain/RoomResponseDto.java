package everyide.webide.room.domain;

import lombok.Builder;
import lombok.Data;

@Data
public class RoomResponseDto {
    private String roomId;
    private String name;

    @Builder
    public RoomResponseDto(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }
}
