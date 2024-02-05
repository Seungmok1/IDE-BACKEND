package everyide.webide.room.domain;

import lombok.Builder;
import lombok.Data;

@Data
public class RoomResponseDto {
    private String roomId;
    private String name;
    private Boolean isLocked;
    private RoomType type;
    private Boolean available;
    @Builder
    public RoomResponseDto(String roomId, String name, Boolean isLocked, RoomType type, Boolean available) {
        this.roomId = roomId;
        this.name = name;
        this.isLocked = isLocked;
        this.type = type;
        this.available = available;
    }
}
