package everyide.webide.fileSystem;

import everyide.webide.fileSystem.domain.dto.CreateDirectoryRequest;
import everyide.webide.fileSystem.domain.dto.DeleteDirectoryRequest;
import everyide.webide.fileSystem.domain.dto.UpdateDirectoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DirectoryController {

    private final DirectoryService directoryService;

    @GetMapping("api/directories")
    public void getDirectory() {

    }

    @PostMapping("api/directories")
    public ResponseEntity<?> createDirectory(@RequestBody CreateDirectoryRequest createDirectoryRequest) {
        directoryService.createDirectory(createDirectoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("디렉토리 생성완료.");
    }

    @PatchMapping("api/directories")
    public ResponseEntity<?> updateDirectory(@RequestBody UpdateDirectoryRequest updateDirectoryRequest) {
        directoryService.updateDirectory(updateDirectoryRequest);
        return ResponseEntity.status(HttpStatus.OK).body("디렉토리 수정완료.");
    }

    @DeleteMapping("api/directories")
    public ResponseEntity<?> deleteDirectory(@RequestBody DeleteDirectoryRequest deleteDirectoryRequest) {
        directoryService.deleteDirectory(deleteDirectoryRequest);
        return ResponseEntity.status(HttpStatus.OK).body("디렉토리 삭제완료.");
    }
}
