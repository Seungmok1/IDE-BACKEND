package everyide.webide.fileSystem.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FileTreeResponse {
    private String name;
    private boolean isDirectory;
    private List<FileTreeResponse> children;

    @Builder
    public FileTreeResponse(String name, boolean isDirectory, List<FileTreeResponse> children) {
        this.name = name;
        this.isDirectory = isDirectory;
        this.children = children;
    }
}
