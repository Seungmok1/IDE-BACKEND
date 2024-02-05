package everyide.webide.room.domain;

import lombok.Getter;

@Getter
public class CreateRoomRequestDto {
    private String name;
    private Boolean isLocked;
    private String password;
    private String roomType;
}
