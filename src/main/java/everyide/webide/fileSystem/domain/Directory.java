package everyide.webide.fileSystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import everyide.webide.BaseEntity;
import everyide.webide.container.domain.Container;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "directories")
public class Directory extends BaseEntity {
    @Id
    private String id;

    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Container container;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Directory parentDirectory;

    @OneToMany(mappedBy = "parentDirectory")
    private List<Directory> childDirectories = new ArrayList<>();

    @OneToMany(mappedBy = "directory")
    private List<File> files = new ArrayList<>();

    @Builder
    public Directory(String path, Directory parentDirectory) {
        this.id = UUID.randomUUID().toString();
        this.path = path;
        this.parentDirectory = parentDirectory;
    }

    public void setContainer(Container container) {
        this.container = container;
        container.addDirectories(this);
    }

    public void setParentDirectory(Directory parentDirectory) {
        this.parentDirectory = parentDirectory;
        parentDirectory.addChildDirectories(this);
    }

    public void addFiles(File file) {
        this.files.add(file);
    }

    public void addChildDirectories(Directory directory) {
        this.childDirectories.add(directory);
    }

    public Directory updateDirectory(String path) {
        this.path = path;

        return this;
    }
}

