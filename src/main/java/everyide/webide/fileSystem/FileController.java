package everyide.webide.fileSystem;

import everyide.webide.fileSystem.domain.dto.FileTreeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/api/{userId}/filetree/{containerName}")
    public FileTreeResponse getFileTree(@PathVariable("userId") Long userId, @PathVariable("containerName") String containerName) {
        return fileService.listFilesAndDirectories(userId, containerName);
    }
}
