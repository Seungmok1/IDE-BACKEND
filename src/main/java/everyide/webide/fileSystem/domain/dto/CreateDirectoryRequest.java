package everyide.webide.fileSystem.domain.dto;

import lombok.Data;

@Data
public class CreateDirectoryRequest {
    private String email;
    private String path;
}
