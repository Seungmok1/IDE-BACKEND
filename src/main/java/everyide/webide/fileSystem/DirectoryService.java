package everyide.webide.fileSystem;

import everyide.webide.fileSystem.domain.Directory;
import everyide.webide.fileSystem.domain.dto.CreateDirectoryRequest;
import everyide.webide.fileSystem.domain.dto.DeleteDirectoryRequest;
import everyide.webide.fileSystem.domain.dto.UpdateDirectoryRequest;
import everyide.webide.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectoryService {

    @Value("${file.basePath}")
    private String basePath;
    private final DirectoryRepository directoryRepository;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;


    public String createDirectory(CreateDirectoryRequest createDirectoryRequest) {

        String path = basePath + createDirectoryRequest.getEmail() + createDirectoryRequest.getPath();
        File directory = new File(path);

        if (!directory.exists()) {
            try {
                directory.mkdir();

                if (!directory.exists()) {
                    return "cant";
                }

                Directory newDirectory = Directory.builder()
                        .path(path)
                        .build();

                directoryRepository.save(newDirectory);
                log.info(path + " 디렉토리 생성.");

                return "ok";
            } catch (Exception e) {
                e.getStackTrace();
            }
        } else {
            log.info(path + " 같은 이름의 디렉토리가 이미 있습니다.");
            return "already";
        }
        return "cant";

    }

    public Directory createRootDirectory(String email) {

        String path = basePath + email;
        File directory = new File(path);

        if (!directory.exists()) {
            try {
                directory.mkdir();

                Directory newDirectory = Directory.builder()
                        .path(path)
                        .build();

                directoryRepository.save(newDirectory);
                log.info(path + " 디렉토리 생성.");

                return newDirectory;
            } catch (Exception e) {
                e.getStackTrace();
            }
        } else {
            log.info(path + " 같은 이름의 디렉토리가 이미 있습니다.");
        }

        return null;

    }

    /*
    내부에 폴더/파일이 있어도 전체 강제 삭제
     */
    public String deleteDirectory(DeleteDirectoryRequest deleteDirectoryRequest) {
        String path = basePath + deleteDirectoryRequest.getEmail() + deleteDirectoryRequest.getPath();
        File directory = new File(path);
        log.info(String.valueOf(deleteDirectoryRequest));
        log.info(path);
        if (directory.exists()) {
            Directory findDir = directoryRepository.findByPath(path)
                    .orElseThrow(() -> new EntityNotFoundException("Directory not found."));

            try {
                FileUtils.deleteDirectory(directory);

                // 데이터베이스에서도 해당 디렉토리 정보 삭제
                log.info(path);
                directoryRepository.deleteByPathAndSubPaths(path);
                fileRepository.deleteByPathAndSubPaths(path);

                return "ok";
            } catch (IOException e) {
                // 디렉토리 삭제 중 발생한 예외 처리
                e.printStackTrace();
                return "cant";
            }
        } else {
            return "cant";
        }
    }

    /*
    * 폴더 이동/이름수정
    * */
    @Transactional
    public String updateDirectory(UpdateDirectoryRequest updateDirectoryRequest) {
        String path = basePath + updateDirectoryRequest.getEmail();
        String fromPath = path + updateDirectoryRequest.getFromPath();
        String toPath = path + updateDirectoryRequest.getToPath();

        File fromDirectory = new File(fromPath);
        File toDirectory = new File(toPath);

        if (!fromDirectory.exists()) {
            return "cant";
        }

        if (toDirectory.exists()) {
            return "cant";
        }

        Directory directory = directoryRepository.findByPath(fromPath)
                .orElseThrow(() -> new EntityNotFoundException("Directory not found."));
        directoryRepository.save(directory.updateDirectory(toPath));

        // 하위 목록 경로 업데이트
        updateSubDirectories(fromDirectory, fromPath, toPath);

        // 실제 디렉토리 이름 변경
        fromDirectory.renameTo(toDirectory);

        return "ok";
    }

    @Transactional
    public void updateSubDirectories(File directory, String oldPath, String newPath) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                String oldFilePath = file.getAbsolutePath();
                String newFilePath = oldFilePath.replace(oldPath, newPath);

                // 하위 파일/디렉토리 경로 업데이트 (데이터베이스 및 파일 시스템)
                updateFilePathInDatabase(oldFilePath, newFilePath);
                file.renameTo(new File(newFilePath));

                // 재귀적으로 하위 디렉토리 처리
                if (file.isDirectory()) {
                    updateSubDirectories(file, oldPath, newPath);
                }
            }
        }
    }

    @Transactional
    public void updateFilePathInDatabase(String oldFilePath, String newFilePath) {
        Optional<Directory> optionalDirectory = directoryRepository.findByPath(oldFilePath);

        if (optionalDirectory.isPresent()) {
            Directory dir = optionalDirectory.get();
            dir.updateDirectory(newFilePath);
            directoryRepository.save(dir);
            return;
        }

        Optional<everyide.webide.fileSystem.domain.File> optionalFile = fileRepository.findByPath(oldFilePath);

        if (optionalFile.isPresent()) {
            everyide.webide.fileSystem.domain.File file = optionalFile.get();
            file.updateFile(newFilePath);
            fileRepository.save(file);
            return;
        }

    }


}
