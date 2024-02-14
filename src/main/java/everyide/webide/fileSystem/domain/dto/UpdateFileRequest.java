package everyide.webide.fileSystem.domain.dto;

import lombok.Data;

@Data
public class UpdateFileRequest {
    private String email;
    private String fromPath;
    private String toPath;
    private String newContent;
}
