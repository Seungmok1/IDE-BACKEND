package everyide.webide.chat.domain;

import everyide.webide.BaseEntity;
import everyide.webide.container.domain.Container;
import everyide.webide.room.domain.Room;
import everyide.webide.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "chats")
public class Chat extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToOne
    private Container container;
    @OneToMany
    private List<User> users;
}
