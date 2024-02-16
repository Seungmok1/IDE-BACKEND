package everyide.webide.fileSystem.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class FileTreeResponse {
    private UUID id;
    private String name;
    private String type;
    private String path;
    private List<FileTreeResponse> children;

    @Builder
    public FileTreeResponse(UUID id, String name, String type, String path, List<FileTreeResponse> children) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.path = path;
        this.children = children;
    }
}
