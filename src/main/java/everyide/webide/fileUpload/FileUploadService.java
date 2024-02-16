package everyide.webide.fileUpload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileUploadService {

    @Value("${file.basePath}")
    private String basePath; // 기본 경로 설정

    public void uploadFile(MultipartFile file, String userEmail, String uploadDir) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        // 사용자 이메일 기반 경로와 추가 경로 결합
        Path uploadPath = Paths.get(basePath + userEmail + "/" + uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Could not upload file: " + fileName, e);
        }
    }
}
