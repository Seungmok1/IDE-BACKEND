package everyide.webide.room.domain;

import everyide.webide.BaseEntity;
import everyide.webide.chat.domain.Chat;
import everyide.webide.container.domain.Container;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "rooms")
public class Room extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;
    private RoomType type;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private Chat chat;
    @OneToMany(mappedBy = "room")
    private List<Container> container;
}
