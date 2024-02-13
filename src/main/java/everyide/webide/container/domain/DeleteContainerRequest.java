package everyide.webide.container.domain;

import lombok.Data;

@Data
public class DeleteContainerRequest {
    private String email;
    private String name;
}
