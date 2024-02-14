package everyide.webide.container;


import everyide.webide.container.domain.ContainerDetailResponse;
import everyide.webide.container.domain.CreateContainerRequest;
import everyide.webide.container.domain.DeleteContainerRequest;
import everyide.webide.container.domain.UpdateContainerRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class ContainerController {

    private final ContainerService containerService;

    @GetMapping("api/{userId}/containers")
    public ResponseEntity<List<ContainerDetailResponse>> getContainers(@PathVariable("userId") Long userId) {
        log.info("여기다");
        return ResponseEntity.ok(containerService.getContainer(userId));
    }

    @PostMapping("api/containers")
    public ResponseEntity<?> createContainers(@RequestBody CreateContainerRequest createContainerRequest) {
        containerService.createContainer(createContainerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("컨테이너 생성완료.");
    }

    @PatchMapping("api/containers")
    public ResponseEntity<?> updateContainers(@RequestBody UpdateContainerRequest updateContainerRequest) {
        containerService.updateContainer(updateContainerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("컨테이너 수정완료.");
    }

    @DeleteMapping("api/containers")
    public ResponseEntity<?> deleteContainers(@RequestBody DeleteContainerRequest deleteContainerRequest) {
        containerService.deleteContainer(deleteContainerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("컨테이너 삭제완료.");
    }

}
