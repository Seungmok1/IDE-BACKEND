package everyide.webide.container;

import everyide.webide.container.domain.ContainerRunRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CodeController {

    private final CodeService codeService;

    @PostMapping("/run")
    public ResponseEntity<?> run(@RequestBody ContainerRunRequestDto requestDto) {
        String path = codeService.saveFile(requestDto);
        String output = switch (requestDto.getProgrammingLanguage()) {
            case "java" -> codeService.runJava(path);
            case "javascript" -> codeService.runJavascript(path);
            case "python" -> codeService.runPython(path);
            default -> throw new IllegalStateException("Unexpected value: " + requestDto.getProgrammingLanguage());
        };

        return ResponseEntity.ok().body(output);
    }
}
