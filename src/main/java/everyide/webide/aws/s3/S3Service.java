package everyide.webide.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import everyide.webide.fileSystem.FileRepository;
import everyide.webide.websocket.compile.domain.CompileRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final FileRepository fileRepository;
    private final AmazonS3 amazonS3;
    private final String bucketName = "everyide-user-code";

    public String uploadS3(CompileRequestDto requestDto, String roomId) throws IOException {
        String code = fileRepository.findByPath(requestDto.getPath()).get().getContent();

        String key = roomId + "/" + requestDto.getPath();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("text/plain");
        metadata.setContentLength(code.length());

        amazonS3.putObject(bucketName, key, inputStream, metadata);

        return key;
    }

    private String getS3(String key) {
        return amazonS3.getUrl(bucketName, key).toString();
    }
}
