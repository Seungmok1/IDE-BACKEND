package everyide.webide.fileSystem.domain.dto;

import lombok.Data;

@Data
public class DeleteDirectoryRequest {
    private String email;
    private String path;

    public DeleteDirectoryRequest(String email, String path) {
        this.email = email;
        this.path = path;
    }
}
