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
    private Boolean fullRoom;
    private String ownerName;
    private Integer personCnt;
    private Integer maxPeople;

    @Builder
    public RoomResponseDto(String roomId, String name, Boolean isLocked, RoomType type, Boolean available, Boolean fullRoom, String ownerName, Integer personCnt, Integer maxPeople) {
        this.roomId = roomId;
        this.name = name;
        this.isLocked = isLocked;
        this.type = type;
        this.available = available;
        this.fullRoom = fullRoom;
        this.ownerName = ownerName;
        this.personCnt = personCnt;
        this.maxPeople = maxPeople;
    }
}
