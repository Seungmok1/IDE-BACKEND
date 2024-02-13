package everyide.webide.container.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import everyide.webide.BaseEntity;
import everyide.webide.room.domain.Room;
import everyide.webide.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Container extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String path;
    private String description;
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Room room;

    @Builder
    public Container(String name, String path, String description, Room room) {
        this.name = name;
        this.path = path;
        this.description = description;
        this.active = true;
        this.room = room;
    }

    //== 연관관계 메서드 ==//
    public void setUser(User user) {
        this.user = user;
        user.addContainer(this);
    }

    public void onContainer() {
        this.active = true;
    }

    public void offContainer() {
        this.active = false;
    }

    public Container updateContainer(String newName, String newPath, String newDescription, boolean active) {
        this.name = newName;
        this.path = newPath;
        this.description = newDescription;
        this.active = active;

        return this;
    }

    public Container updateContainer(String newDescription, boolean active) {
        this.description = newDescription;
        this.active = active;

        return this;
    }
}
