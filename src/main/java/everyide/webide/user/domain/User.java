package everyide.webide.user.domain;

import everyide.webide.BaseEntity;
import everyide.webide.config.auth.user.oauth2.AuthProvider;
import everyide.webide.config.auth.user.oauth2.OAuth2UserInfo;
import everyide.webide.container.domain.Container;
import everyide.webide.room.domain.Room;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String picture;

    private String email;

    private String password;

    private String role;

    private String rootPath;

    @Setter
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    @OneToMany(mappedBy = "user")
    private List<Container> containers = new ArrayList<>();

    @OneToOne
    private Room room;

    @ElementCollection
    private List<String> roomsList;

    @Builder
    public User(Long id, String name, String picture, String email, String password, String role, String refreshToken, AuthProvider provider, String providerId) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.email = email;
        this.password = password;
        this.role = role;
        this.refreshToken = refreshToken;
        this.provider = provider;
        this.providerId = providerId;
    }

    public static User createNewUser(AuthProvider provider, String providerId, String name, String email, String imageUrl, String role) {
        return User.builder()
                .provider(provider)
                .providerId(providerId)
                .name(name)
                .email(email)
                .picture(imageUrl)
                .role(role)
                .build();
    }

    public void updateOAuth2UserInfo(OAuth2UserInfo oAuth2UserInfo) {
        this.name = oAuth2UserInfo.getName();
        this.picture = oAuth2UserInfo.getImageUrl();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRootPath(String path) {
        this.rootPath = path;
    }


    //== 연관관계 메서드 ==//
    public void addContainer(Container container) {
        this.containers.add(container);
    }

    public void removeContainer(Container container) {
        this.containers.remove(container);
    }

    public void setRoom(Room room) {
        this.room = room;
    }

}
