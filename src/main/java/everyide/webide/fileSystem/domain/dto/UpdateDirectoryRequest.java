package everyide.webide.fileSystem.domain.dto;

import lombok.Data;

@Data
public class UpdateDirectoryRequest {
    private String email;
    private String fromPath;
    private String toPath;
}
