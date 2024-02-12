package everyide.webide.container.domain;

import lombok.Getter;

@Getter
public class CodeRequestDto {
    private String code;
    private String programmingLanguage;
    private String roomId;
    private String fileName;
}
