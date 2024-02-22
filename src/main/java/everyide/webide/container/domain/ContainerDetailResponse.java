package everyide.webide.container.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContainerDetailResponse {
    private Long id;
    private String name;
    private String description;
    private String language;
    private boolean active;
    private int shared;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;

    @Builder
    public ContainerDetailResponse(Long id, String name, String description, String language, boolean active, int shared, LocalDateTime createDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.language = language;
        this.active = active;
        this.shared = shared;
        this.createDate = createDate;
        this.lastModifiedDate = lastModifiedDate;
    }
}
