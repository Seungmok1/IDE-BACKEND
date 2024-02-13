package everyide.webide.container.domain;

import lombok.Data;

@Data
public class CreateContainerRequest {
    private String email;
    private String name;
    private String description;
//    private String language;
}
