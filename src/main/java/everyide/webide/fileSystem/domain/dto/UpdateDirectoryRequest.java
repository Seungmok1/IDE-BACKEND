package everyide.webide.fileSystem.domain.dto;

import lombok.Data;

@Data
public class UpdateDirectoryRequest {
    private String email;
    private String fromPath;
    private String toPath;

    public UpdateDirectoryRequest(String email, String fromPath, String toPath) {
        this.email = email;
        this.fromPath = fromPath;
        this.toPath = toPath;
    }
}
