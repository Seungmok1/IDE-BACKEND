package everyide.webide.container;

import everyide.webide.container.domain.*;
import everyide.webide.fileSystem.DirectoryRepository;
import everyide.webide.fileSystem.DirectoryService;
import everyide.webide.fileSystem.FileService;
import everyide.webide.fileSystem.domain.Directory;
import everyide.webide.fileSystem.domain.dto.DeleteDirectoryRequest;
import everyide.webide.fileSystem.domain.dto.UpdateDirectoryRequest;
import everyide.webide.user.UserRepository;
import everyide.webide.user.domain.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContainerService {

    @Value("${file.basePath}")
    private String basePath;
    private final ContainerRepository containerRepository;
    private final DirectoryRepository directoryRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final DirectoryService directoryService;

    public List<ContainerDetailResponse> getContainer(Long userId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return findUser.getContainers().stream()
                .map(container -> ContainerDetailResponse.builder()
                        .id(container.getId())
                        .name(container.getName())
                        .description(container.getDescription())
                        .language(container.getLanguage())
                        .active(container.isActive())
                        .createDate(container.getCreateDate())
                        .lastModifiedDate(container.getLastModifiedDate())
                        .build())
                .collect(Collectors.toList());

    }

    public String createContainer(CreateContainerRequest createContainerRequest) {
        Optional<User> findUserOptional = userRepository.findByEmail(createContainerRequest.getEmail());

        if (findUserOptional.isPresent()) {
            User findUser = findUserOptional.get();
            String path = basePath + createContainerRequest.getEmail() + "/" + createContainerRequest.getName();
            File container = new File(path);

            if (!container.exists()) {
                boolean isCreated = container.mkdir();
                if (isCreated) {
                    try {
                        Container newContainer = Container.builder()
                                .name(createContainerRequest.getName())
                                .description(createContainerRequest.getDescription())
                                .path(path)
                                .language(createContainerRequest.getLanguage())
                                .build();

                        newContainer.setUser(findUser);
                        containerRepository.save(newContainer);
                        Directory directory = Directory.builder()
                                .path(path)
                                .build();
                        directoryRepository.save(directory);
                        fileService.createDefaultFile(path, createContainerRequest.getLanguage());
                        return "ok";
                    } catch (Exception e) {
                        // 로그를 남기고 오류 메시지 반환
                        e.printStackTrace(); // 개발 환경에서만 사용, 프로덕션 코드에는 적절한 로깅을 사용하세요.
                        return "error";
                    }
                } else {
                    return "cant";
                }
            } else {
                return "already used";
            }
        } else {
            return "cant";
        }
    }


    public boolean deleteContainer(DeleteContainerRequest deleteContainerRequest) {
        String path = basePath + deleteContainerRequest.getEmail() + "/" + deleteContainerRequest.getName();
        File container = new File(path);

        if (container.exists()) {
            Container findContainer = containerRepository.findByPath(path)
                    .orElseThrow(() -> new EntityNotFoundException("Container not found."));

            findContainer.getUser().removeContainer(findContainer);
            containerRepository.delete(findContainer);
            directoryService.deleteDirectory(new DeleteDirectoryRequest(deleteContainerRequest.getEmail(), "/" + deleteContainerRequest.getName()));

            return true;
        } else {
            return false;
        }
    }

    /*
     * 컨테이너 수정
     * */
    @Transactional
    public String updateContainer(UpdateContainerRequest updateContainerRequest) {
        String path = basePath + updateContainerRequest.getEmail();
        String oldPath = path + "/" + updateContainerRequest.getOldName();
        String newPath = path + "/" + updateContainerRequest.getNewName();

        File oldContainer = new File(oldPath);
        File newContainer = new File(newPath);

        if (oldContainer.exists()) {
            if (!oldPath.equals(newPath) && newContainer.exists()) {
                return "already used";
            }

            Container container = containerRepository.findByPath(oldPath)
                    .orElseThrow(() -> new EntityNotFoundException("Container not found."));

            containerRepository.save(container.updateContainer(updateContainerRequest.getNewName(), newPath, updateContainerRequest.getNewDescription(), updateContainerRequest.isActive()));
//            oldContainer.renameTo(newContainer);

            directoryService.updateDirectory(new UpdateDirectoryRequest(updateContainerRequest.getEmail(), "/" + updateContainerRequest.getOldName(), "/" + updateContainerRequest.getNewName()));

            return "ok";
        } else {
            return "not found";
        }
    }


}
