package everyide.webide.fileSystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import everyide.webide.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Directory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private String name;
    private String path;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Directory parentDirectory;

    @OneToMany(mappedBy = "parentDirectory")
    private List<Directory> childDirectories = new ArrayList<>();

    @OneToMany(mappedBy = "directory")
    private List<File> files = new ArrayList<>();

    @Builder
    public Directory(String path, Directory parentDirectory) {
        this.path = path;
        this.parentDirectory = parentDirectory;
    }

    public Directory updateDirectory(String path) {
        this.path = path;

        return this;
    }
}

