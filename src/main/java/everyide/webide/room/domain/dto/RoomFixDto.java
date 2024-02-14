package everyide.webide.room.domain.dto;

import lombok.Data;

@Data
public class RoomFixDto {

    private String name;
    private String password;
    private Boolean isLocked;
}
