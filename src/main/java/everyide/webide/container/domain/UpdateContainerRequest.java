package everyide.webide.container.domain;

import lombok.Data;

@Data
public class UpdateContainerRequest {
    private String email;
    private String oldName;
    private String newName;
    private String newDescription;
    private boolean active;
}
