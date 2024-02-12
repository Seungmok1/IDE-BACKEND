package everyide.webide.container.domain;

import lombok.Getter;

@Getter
public class ContainerRunRequestDto {
    private String code;
    private String programmingLanguage;
    private String projectName;
    private String fileName;
}
