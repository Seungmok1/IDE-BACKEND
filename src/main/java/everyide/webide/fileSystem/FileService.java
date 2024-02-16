package everyide.webide.fileSystem;

import everyide.webide.container.ContainerRepository;
import everyide.webide.container.ContainerService;
import everyide.webide.container.domain.Container;
import everyide.webide.fileSystem.domain.dto.*;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${file.basePath}")
    private String basePath;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final ContainerRepository containerRepository;

    public GetFileResponse getFile(Long containerId, String path) {
        Optional<Container> containerOptional = containerRepository.findById(containerId);

        if (containerOptional.isPresent()) {
            Container container = containerOptional.get();
            String filePath = container.getPath() + path;

            if (!Files.exists(Paths.get(filePath))) {
                return null;
            }

            try {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                return new GetFileResponse(content);
            } catch (IOException e) {
                // IOException 처리
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public void createFile(CreateFileRequest createFileRequest) {
        String path = basePath + createFileRequest.getEmail() + createFileRequest.getPath();
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
                        .orElseThrow(() -> new EntityNotFoundException("File not found."));
                fileRepository.save(findFile.updateFile(newPath));

                log.info(oldPath + " 파일이 " + newPath + "로 이름이 변경되었습니다.");
            } else {
                log.error("파일 이름 변경 실패");
            }
        }
    }


    public void deleteFile(DeleteFileRequest deleteFileRequest) {
        String path = basePath + deleteFileRequest.getEmail() + deleteFileRequest.getPath();
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

    public void createDefaultFile(String path, String language) {
        String fileName = null;
        String defaultContent = null;

        if (language.equals("java")) {
            fileName = "Main.java";
            defaultContent = "public class Main {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, World!\");\n    }\n}";

        } else if (language.equals("javascript")) {
            fileName = "main.js";
            defaultContent = "console.log('Hello, World!');";
        } else {
            fileName = "main.py";
            defaultContent = "print('Hello, World!')";
        }

        File file = new File(path, fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(defaultContent);
            writer.flush();

            everyide.webide.fileSystem.domain.File defaultFile = everyide.webide.fileSystem.domain.File.builder()
                    .path(path + fileName)
                    .build();
            fileRepository.save(defaultFile);

        } catch (IOException e) {
            e.printStackTrace(); // 파일 생성 실패시 로그에 스택 트레이스 출력
        }

    }

    public String extractPathAfterEmail(String fullPath) {
        String[] parts = fullPath.split("/");

        int emailIndex = -1;
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].contains("@")) {
                emailIndex = i;
                break;
            }
        }

        if (emailIndex != -1 && emailIndex + 1 < parts.length) {
            StringBuilder sb = new StringBuilder();
            for (int i = emailIndex + 2; i < parts.length; i++) {
                sb.append("/");
                sb.append(parts[i]);
            }
            return sb.toString();
        }

        return "";
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
        String path = extractPathAfterEmail(directory.getPath()).isEmpty() ? "/" : extractPathAfterEmail(directory.getPath());

        FileTreeResponse fileInfo = new FileTreeResponse(UUID.randomUUID(), directory.getName(), type, path, new ArrayList<>());

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
