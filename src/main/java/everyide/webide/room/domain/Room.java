package everyide.webide.room.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import everyide.webide.BaseEntity;
import everyide.webide.container.domain.Container;
import everyide.webide.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
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
    private String description;
    private String password;
    private RoomType type;
    private Boolean available = true;
    private String rootPath;
    @Setter
    private Integer maxPeople;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User owner;

    @OneToMany(mappedBy = "room")
    private List<Container> containers = new ArrayList<>();

    @ElementCollection
    private List<Long> usersId;

    @Builder
    public Room(Boolean isLocked, String description, String name, String password, RoomType type, Boolean available, User owner, Integer maxPeople, List<Long> usersId) {
        id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.password = password;
        this.isLocked = isLocked;
        this.type = type;
        this.maxPeople = maxPeople;
        this.usersId = usersId;
        this.owner = owner;
        // 사용자가 available 값을 명시적으로 설정한 경우 해당 값을 사용
        if (available != null) {
            this.available = available;
        }
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setRootPath(String path) {
        this.rootPath = path;
    }

    public void addContainer(Container container) {
        this.containers.add(container);
    }

    public void removeContainer(Container container) {
        this.containers.remove(container);
    }
}
