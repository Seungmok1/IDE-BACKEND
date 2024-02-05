package everyide.webide.room.domain;

import everyide.webide.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "rooms")
public class Room extends BaseEntity {

    @Id
    private String id;
    private Boolean isLocked;
    private String name;
    private String password;
    private RoomType type;
    private Boolean available = true;

    @Builder
    public Room(Boolean isLocked, String name, String password, RoomType type, Boolean available) {
        id = UUID.randomUUID().toString();
        this.name = name;
        this.password = password;
        this.isLocked = isLocked;
        this.type = type;
        // 사용자가 available 값을 명시적으로 설정한 경우 해당 값을 사용
        if (available != null) {
            this.available = available;
        }
    }
}
