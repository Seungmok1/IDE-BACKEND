package everyide.webide.fileSystem;

import everyide.webide.fileSystem.domain.dto.CreateDirectoryRequest;
import everyide.webide.fileSystem.domain.dto.DeleteDirectoryRequest;
import everyide.webide.fileSystem.domain.dto.UpdateDirectoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DirectoryController {

    private final DirectoryService directoryService;

    @GetMapping("api/directories")
    public void getDirectory() {

    }

    @PostMapping("api/directories")
    public void createDirectory(@RequestBody CreateDirectoryRequest createDirectoryRequest) {
        directoryService.createDirectory(createDirectoryRequest);
    }

    @PatchMapping("api/directories")
    public void updateDirectory(@RequestBody UpdateDirectoryRequest updateDirectoryRequest) {
        directoryService.updateDirectory(updateDirectoryRequest);
    }

    @DeleteMapping("api/directories")
    public void deleteDirectory(@RequestBody DeleteDirectoryRequest deleteDirectoryRequest) {
        directoryService.deleteDirectory(deleteDirectoryRequest);
    }
}
