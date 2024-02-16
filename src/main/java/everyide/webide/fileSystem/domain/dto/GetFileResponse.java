package everyide.webide.fileSystem.domain.dto;

import lombok.Data;

@Data
public class GetFileResponse {
    private String content;

    public GetFileResponse(String content) {
        this.content = content;
    }
}
