package everyide.webide.container.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import everyide.webide.BaseEntity;
import everyide.webide.fileDirectory.domain.Directory;
import everyide.webide.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "containers")
public class Container extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name; // 프로젝트 이름
    private String description; // 설명
    private boolean activeStatus; // 활성상태

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User owner; // 프로젝트 소유자
    @OneToMany
    private List<Directory> directory;
}
