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
    private String ownerName;
    private Integer maxPeople;

    @Builder
    public RoomResponseDto(String roomId, String name, Boolean isLocked, RoomType type, Boolean available, String ownerName, Integer maxPeople) {
        this.roomId = roomId;
        this.name = name;
        this.isLocked = isLocked;
        this.type = type;
        this.available = available;
        this.ownerName = ownerName;
        this.maxPeople = maxPeople;
    }
}
