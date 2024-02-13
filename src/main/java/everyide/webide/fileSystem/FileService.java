package everyide.webide.fileSystem;

import everyide.webide.fileSystem.domain.dto.CreateFileRequest;
import everyide.webide.fileSystem.domain.dto.DeleteFileRequest;
import everyide.webide.fileSystem.domain.dto.FileTreeResponse;
import everyide.webide.fileSystem.domain.dto.UpdateFileRequest;
import everyide.webide.user.UserRepository;
import everyide.webide.user.domain.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${file.basePath}")
    private String basePath;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    public void createFile(CreateFileRequest createFileRequest) {
    }

    public void updateFile(UpdateFileRequest updateFileRequest) {

    }

    public void deleteFile(DeleteFileRequest deleteFileRequest) {

    }

    public FileTreeResponse listFilesAndDirectories(Long userId, String containerName) {

        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
        String path = basePath + findUser.getEmail() + "/" + containerName;

        File root = new File(path);
        return listFilesAndDirectoriesRecursive(root);
    }

    private FileTreeResponse listFilesAndDirectoriesRecursive(File directory) {
        FileTreeResponse fileInfo = new FileTreeResponse(directory.getName(), directory.isDirectory(), new ArrayList<>());

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                FileTreeResponse child = listFilesAndDirectoriesRecursive(file);
                fileInfo.getChildren().add(child);
            }
        }

        return fileInfo;
    }
}
