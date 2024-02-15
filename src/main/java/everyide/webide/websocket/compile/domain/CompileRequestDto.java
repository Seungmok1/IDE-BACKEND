package everyide.webide.websocket.compile.domain;

import lombok.Getter;

@Getter
public class CompileRequestDto {
    private String path;
    private String fileName;
    private String programmingLanguage;
}
