package everyide.webide.container.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContainerDetailResponse {
    private String name;
    private String description;
    private String language;
    private boolean active;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;

    @Builder
    public ContainerDetailResponse(String name, String description, String language, boolean active, LocalDateTime createDate, LocalDateTime lastModifiedDate) {
        this.name = name;
        this.description = description;
        this.language = language;
        this.active = active;
        this.createDate = createDate;
        this.lastModifiedDate = lastModifiedDate;
    }
}
