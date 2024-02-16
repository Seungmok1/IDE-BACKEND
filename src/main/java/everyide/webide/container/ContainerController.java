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
        return ResponseEntity.ok(containerService.getContainer(userId));
    }

    @PostMapping("api/containers")
    public ResponseEntity<?> createContainers(@RequestBody CreateContainerRequest createContainerRequest) {
        String status = containerService.createContainer(createContainerRequest);
        if (status.equals("ok")) {
            return ResponseEntity.status(HttpStatus.CREATED).body("컨테이너 생성완료.");
        } else if (status.equals("already used")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 사용중인 이름입니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 경로입니다. (사용자 이메일 확인)");
        }

    }

    @PatchMapping("api/containers")
    public ResponseEntity<?> updateContainers(@RequestBody UpdateContainerRequest updateContainerRequest) {
        String status = containerService.updateContainer(updateContainerRequest);
        if (status.equals("ok")) {
            return ResponseEntity.status(HttpStatus.OK).body("컨테이너 수정완료.");
        } else if (status.equals("already used")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 사용중인 이름입니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("컨테이너를 찾을 수 없습니다.");
        }

    }

    @DeleteMapping("api/containers")
    public ResponseEntity<?> deleteContainers(@RequestBody DeleteContainerRequest deleteContainerRequest) {
        if (containerService.deleteContainer(deleteContainerRequest)) {
            return ResponseEntity.status(HttpStatus.OK).body("컨테이너 삭제완료.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("컨테이너 삭제실패.");
        }
    }

}
