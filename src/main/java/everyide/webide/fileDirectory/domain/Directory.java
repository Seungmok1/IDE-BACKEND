package everyide.webide.fileDirectory.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import everyide.webide.BaseEntity;
import everyide.webide.container.domain.Container;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "directorys")
public class Directory extends BaseEntity {

    /**
     * root 폴더일 경우 parentDirectoryId == NULL
     */

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long parentDirectoryId;
    private String name;
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Container container;
    @OneToMany
    private List<File> files = new ArrayList<>();

}
