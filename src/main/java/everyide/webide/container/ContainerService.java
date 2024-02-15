package everyide.webide.container;

import everyide.webide.container.domain.*;
import everyide.webide.user.UserRepository;
import everyide.webide.user.domain.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContainerService {

    @Value("${file.basePath}")
    private String basePath;
    private final ContainerRepository containerRepository;
    private final UserRepository userRepository;

    public List<ContainerDetailResponse> getContainer(Long userId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return findUser.getContainers().stream()
                .map(container -> ContainerDetailResponse.builder()
                        .name(container.getName())
                        .description(container.getDescription())
                        .language(container.getLanguage())
                        .active(container.isActive())
                        .createDate(container.getCreateDate())
                        .lastModifiedDate(container.getLastModifiedDate())
                        .build())
                .collect(Collectors.toList());

    }

    public void createContainer(CreateContainerRequest createContainerRequest) {

        User findUser = userRepository.findByEmail(createContainerRequest.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        String path = basePath + createContainerRequest.getEmail() + "/" + createContainerRequest.getName();
        File container = new File(path);

        if (!container.exists()) {
            try {
                container.mkdir();

                Container newContainer = Container.builder()
                        .name(createContainerRequest.getName())
                        .description(createContainerRequest.getDescription())
                        .path(path)
                        .language(createContainerRequest.getLanguage())
                        .build();

                newContainer.setUser(findUser);
                containerRepository.save(newContainer);
                log.info(createContainerRequest.getName() + " : 컨테이너 생성.");

            } catch (Exception e) {
                e.getStackTrace();
            }
        } else {
            log.info(createContainerRequest.getName() + " : 같은 이름의 컨테이너가 이미 있습니다.");
        }

    }

    public String deleteContainer(DeleteContainerRequest deleteContainerRequest) {
        String path = basePath + deleteContainerRequest.getEmail() + "/" + deleteContainerRequest.getName();
        File container = new File(path);

        if (container.exists()) {
            Container findContainer = containerRepository.findByPath(path)
                    .orElseThrow(() -> new EntityNotFoundException("Container not found."));

            try {
                FileUtils.deleteDirectory(container);

                // 데이터베이스에서도 해당 컨테이너 정보 삭제
                containerRepository.delete(findContainer);

                return path + " 삭제완료.";
            } catch (IOException e) {
                // 컨테이너 삭제 중 발생한 예외 처리
                e.printStackTrace();
                return "삭제 실패: " + path + " (오류 메시지: " + e.getMessage() + ")";
            }
        } else {
            return "삭제 실패: " + path + " (컨테이너가 존재하지 않습니다.)";
        }
    }

    /*
     * 컨테이너 수정
     * */
    public String updateContainer(UpdateContainerRequest updateContainerRequest) {
        String path = basePath + updateContainerRequest.getEmail();
        String oldPath = path + "/" + updateContainerRequest.getOldName();
        String newPath = path + "/" + updateContainerRequest.getNewName();

        File oldContainer = new File(oldPath);
        File newContainer = new File(newPath);

        if (oldContainer.exists()) {

            Container container = containerRepository.findByPath(oldPath)
                    .orElseThrow(() -> new EntityNotFoundException("Container not found."));

            if (oldPath.equals(newPath)) {
                containerRepository.save(container.updateContainer(updateContainerRequest.getNewDescription(), updateContainerRequest.isActive()));
            } else {
                containerRepository.save(container.updateContainer(updateContainerRequest.getNewName(), newPath, updateContainerRequest.getNewDescription(), container.isActive()));
                oldContainer.renameTo(newContainer);
            }


            return "컨테이너 수정 완료";
        } else {
            return "컨테이너 수정 불가";
        }
    }


}
