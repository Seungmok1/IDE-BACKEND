package everyide.webide.fileSystem.domain.dto;

import lombok.Data;

@Data
public class DeleteFileRequest {
    private String email;
    private String path;
}
