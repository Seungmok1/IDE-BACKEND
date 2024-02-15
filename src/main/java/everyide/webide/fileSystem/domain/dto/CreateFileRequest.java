package everyide.webide.fileSystem.domain.dto;

import lombok.Data;

@Data
public class CreateFileRequest {
    private String email;
    private String path;
    private String content;
}
