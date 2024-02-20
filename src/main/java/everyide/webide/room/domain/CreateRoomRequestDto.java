package everyide.webide.room.domain;

import lombok.Data;

@Data
public class CreateRoomRequestDto {
    private String name;
    private Boolean isLocked;
    private String password;
    private String roomType;
    private Integer maxPeople;
    private Boolean createType;

    private String containerName;
    private String containerDescription;
    private String containerLanguage;
}