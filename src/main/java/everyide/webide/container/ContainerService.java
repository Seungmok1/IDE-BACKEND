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

    public ContainerDetailResponse createContainer(CreateContainerRequest createContainerRequest) {
        Optional<User> findUserOptional = userRepository.findByEmail(createContainerRequest.getEmail());

        if (!findUserOptional.isPresent()) {
            return ContainerDetailResponse.builder()
                    .id(-200L)
                    .build();
        }

        User findUser = findUserOptional.get();
        String path = basePath + createContainerRequest.getEmail() + "/" + createContainerRequest.getName();
        File container = new File(path);

        if (container.exists()) {
            return ContainerDetailResponse.builder()
                    .id(-300L)
                    .build();
        }

        boolean isCreated = container.mkdir();
        if (!isCreated) {
            return ContainerDetailResponse.builder()
                    .id(-200L)
                    .build();
        }

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

            return ContainerDetailResponse.builder()
                    .id(newContainer.getId())
                    .name(newContainer.getName())
                    .description(newContainer.getDescription())
                    .language(newContainer.getLanguage())
                    .active(newContainer.isActive())
                    .createDate(newContainer.getCreateDate())
                    .lastModifiedDate(newContainer.getLastModifiedDate())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ContainerDetailResponse.builder()
                    .id(-200L)
                    .build();
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

//    @Transactional
//    public ContainerDetailResponse copyContainer(Long sourceContainerId, String newContainerName, String newContainerDescription, String userEmail) {
//        // 원본 컨테이너 찾기
//        Container sourceContainer = containerRepository.findById(sourceContainerId)
//                .orElseThrow(() -> new EntityNotFoundException("Source container not found."));
//
//        // 새 컨테이너 경로 설정
//        String newPath = basePath + userEmail + "/" + newContainerName;
//        File newContainerDir = new File(newPath);
//        if (newContainerDir.exists()) {
//            throw new IllegalStateException("Target container already exists.");
//        }
//
//        // 원본 컨테이너의 내용을 새 경로로 복사
//        try {
//            FileUtils.copyDirectory(new File(sourceContainer.getPath()), newContainerDir);
//        } catch (IOException e) {
//            log.error("Failed to copy container directory", e);
//            throw new RuntimeException("Failed to copy container.");
//        }
//
//        // 새 컨테이너 데이터베이스 엔트리 생성
//        Container newContainer = Container.builder()
//                .name(newContainerName)
//                .description(newContainerDescription)
//                .path(newPath)
//                .language(sourceContainer.getLanguage()) // 원본 컨테이너의 언어 설정을 사용할 수 있습니다.
//                .user(userRepository.findByEmail(userEmail)
//                        .orElseThrow(() -> new EntityNotFoundException("User not found.")))
//                .build();
//
//        containerRepository.save(newContainer);
//
//        // 새 디렉토리 데이터베이스 엔트리 생성
//        Directory newDirectory = Directory.builder()
//                .path(newPath)
//                .build();
//        directoryRepository.save(newDirectory);
//
//        // 필요한 경우, 새 컨테이너에 기본 파일 생성
//        fileService.createDefaultFile(newPath, newContainer.getLanguage());
//
//        // 성공 응답 반환
//        return ContainerDetailResponse.builder()
//                .id(newContainer.getId())
//                .name(newContainer.getName())
//                .description(newContainer.getDescription())
//                .language(newContainer.getLanguage())
//                .active(newContainer.isActive())
//                .createDate(newContainer.getCreateDate())
//                .lastModifiedDate(newContainer.getLastModifiedDate())
//                .build();
//    }


}
