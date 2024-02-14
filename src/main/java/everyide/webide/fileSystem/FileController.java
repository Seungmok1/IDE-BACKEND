package everyide.webide.fileSystem;

import everyide.webide.fileSystem.domain.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/api/{userId}/filetree/{containerName}")
    public FileTreeResponse getFileTree(@PathVariable("userId") Long userId, @PathVariable("containerName") String containerName) {
        return fileService.listFilesAndDirectories(userId, containerName);
    }

    @PostMapping("api/files")
    public void createFile(@RequestBody CreateFileRequest createFileRequest) {
        fileService.createFile(createFileRequest);
    }

    @PatchMapping("api/files")
    public void updateFile(@RequestBody UpdateFileRequest updateFileRequest) {
        fileService.updateFile(updateFileRequest);
    }

    @DeleteMapping("api/files")
    public void deleteFile(@RequestBody DeleteFileRequest deleteFileRequest) {
        fileService.deleteFile(deleteFileRequest);
    }
}
