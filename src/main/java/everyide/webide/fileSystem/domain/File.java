package everyide.webide.fileSystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import everyide.webide.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class File extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private String name;
    private String path;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Directory directory;

    @Builder
    public File(String path, String content) {
        this.path = path;
        this.content = content;
    }
}
