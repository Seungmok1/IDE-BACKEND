package everyide.webide.room.domain;

import everyide.webide.BaseEntity;
import jakarta.annotation.Nullable;
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

    @Builder
    public Room(Boolean isLocked, String name, String password, RoomType type) {
        id = UUID.randomUUID().toString();
        this.name = name;
        this.password = password;
        this.isLocked = isLocked;
        this.type = type;
    }
}
