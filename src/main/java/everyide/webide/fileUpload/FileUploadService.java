package everyide.webide.fileUpload;


import everyide.webide.fileSystem.FileRepository;
import everyide.webide.fileSystem.domain.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class FileUploadService {

    @Value("${file.basePath}")
    private String basePath; // 기본 경로 설정

    @Autowired
    private FileRepository fileRepository; // 파일 정보를 다루는 리포지토리

    @Transactional
    public void uploadFile(MultipartFile file, String userEmail, String uploadDir) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        // 사용자 이메일 기반 경로와 추가 경로 결합
        Path uploadPath = Paths.get(basePath + userEmail + "/" + uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Could not upload file: " + fileName, e);
        }

        // 파일 내용 읽기
        String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);

        // DB에 파일 정보 저장
        File fileEntity = new File();
        fileEntity.setPath(filePath.toString());
        fileEntity.setContent(content);
        fileRepository.save(fileEntity);

        log.info("{} 파일이 업로드되고 DB에 저장되었습니다.", fileName);
    }
}
