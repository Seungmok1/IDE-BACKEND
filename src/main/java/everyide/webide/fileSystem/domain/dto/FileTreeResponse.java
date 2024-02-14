package everyide.webide.fileSystem.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FileTreeResponse {
    private String name;
    private String type;
    private List<FileTreeResponse> children;

    @Builder
    public FileTreeResponse(String name, String type, List<FileTreeResponse> children) {
        this.name = name;
        this.type = type;
        this.children = children;
    }
}
