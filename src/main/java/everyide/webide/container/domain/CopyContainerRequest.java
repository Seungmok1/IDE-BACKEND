package everyide.webide.container.domain;

import lombok.Data;

@Data
public class CopyContainerRequest {
    private String roomId;
    private Long containerId;
}
