package everyide.webide.fileSystem;

import everyide.webide.fileSystem.domain.dto.CreateFileRequest;
import everyide.webide.fileSystem.domain.dto.DeleteFileRequest;
import everyide.webide.fileSystem.domain.dto.FileTreeResponse;
import everyide.webide.fileSystem.domain.dto.UpdateFileRequest;
import everyide.webide.user.UserRepository;
import everyide.webide.user.domain.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${file.basePath}")
    private String basePath;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    public void createFile(CreateFileRequest createFileRequest) {
        String path = basePath + createFileRequest.getEmail() + "/" + createFileRequest.getPath();
        File file = new File(path);

        try {
            if (file.createNewFile()) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(createFileRequest.getContent());
                }
                everyide.webide.fileSystem.domain.File newFile = everyide.webide.fileSystem.domain.File.builder()
                        .path(path)
                        .build();
                fileRepository.save(newFile);

                log.info(path + " 파일이 생성되었습니다.");
            } else {
                log.info(path + " 파일은 이미 존재합니다.");
            }
        } catch (IOException e) {
            log.error("파일 생성 중 오류 발생", e);
        }
    }

    public void updateFile(UpdateFileRequest updateFileRequest) {
        String oldPath = basePath + updateFileRequest.getEmail() + updateFileRequest.getFromPath();
        File oldFile = new File(oldPath);

        if (updateFileRequest.getNewContent() != null && !updateFileRequest.getNewContent().isEmpty()) {
            try (FileWriter writer = new FileWriter(oldFile, false)) { // false to overwrite
                writer.write(updateFileRequest.getNewContent());
                log.info(oldPath + " 파일이 업데이트되었습니다.");
            } catch (IOException e) {
                log.error("파일 업데이트 중 오류 발생", e);
            }
        }

        if (updateFileRequest.getToPath() != null && !updateFileRequest.getToPath().isEmpty()) {
            String newPath = basePath + updateFileRequest.getEmail() + updateFileRequest.getToPath();
            File newFile = new File(newPath);

            if (oldFile.renameTo(newFile)) {

                everyide.webide.fileSystem.domain.File findFile = fileRepository.findByPath(oldPath)
                        .orElseThrow();
                fileRepository.save(findFile.updateFile(newPath));

                log.info(oldPath + " 파일이 " + newPath + "로 이름이 변경되었습니다.");
            } else {
                log.error("파일 이름 변경 실패");
            }
        }
    }


    public void deleteFile(DeleteFileRequest deleteFileRequest) {
        String path = basePath + deleteFileRequest.getEmail() + "/" + deleteFileRequest.getPath();
        File file = new File(path);

        if (file.delete()) {
            everyide.webide.fileSystem.domain.File findFile = fileRepository.findByPath(path)
                    .orElseThrow();
            fileRepository.delete(findFile);

            log.info(path + " 파일이 삭제되었습니다.");
        } else {
            log.error("파일 삭제 실패");
        }
    }


    public FileTreeResponse listFilesAndDirectories(Long userId, String containerName) {

        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
        String path = basePath + findUser.getEmail() + "/" + containerName;

        File root = new File(path);
        return listFilesAndDirectoriesRecursive(root);
    }

    private FileTreeResponse listFilesAndDirectoriesRecursive(File directory) {
        String type = directory.isDirectory() ? "directory" : "file";

        FileTreeResponse fileInfo = new FileTreeResponse(directory.getName(), type, new ArrayList<>());

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {

                if (".DS_Store".equals(file.getName())) {
                    continue;
                }

                FileTreeResponse child = listFilesAndDirectoriesRecursive(file);
                fileInfo.getChildren().add(child);
            }
        }

        return fileInfo;
    }
}
