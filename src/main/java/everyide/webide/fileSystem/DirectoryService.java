package everyide.webide.fileSystem;

import everyide.webide.fileSystem.domain.Directory;
import everyide.webide.fileSystem.domain.dto.CreateDirectoryRequest;
import everyide.webide.fileSystem.domain.dto.DeleteDirectoryRequest;
import everyide.webide.fileSystem.domain.dto.UpdateDirectoryRequest;
import everyide.webide.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectoryService {

    private final String basePath = "/home/ec2-user/everyDataBase/";

    private final DirectoryRepository directoryRepository;
    private final UserRepository userRepository;


    public Directory createDirectory(CreateDirectoryRequest createDirectoryRequest) {

        String path = basePath + createDirectoryRequest.getEmail() + createDirectoryRequest.getPath();
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

        if (directory.exists()) {
            Directory findDir = directoryRepository.findByPath(path)
                    .orElseThrow(() -> new EntityNotFoundException("Directory not found."));

            try {
                FileUtils.deleteDirectory(directory);

                // 데이터베이스에서도 해당 디렉토리 정보 삭제
                directoryRepository.delete(findDir);

                return path + " 삭제완료.";
            } catch (IOException e) {
                // 디렉토리 삭제 중 발생한 예외 처리
                e.printStackTrace();
                return "삭제 실패: " + path + " (오류 메시지: " + e.getMessage() + ")";
            }
        } else {
            return "삭제 실패: " + path + " (디렉토리가 존재하지 않습니다.)";
        }
    }

    /*
    * 폴더 이동/이름수정
    * */
    public String updateDirectory(UpdateDirectoryRequest updateDirectoryRequest) {
        String path = basePath + updateDirectoryRequest.getEmail();
        String fromPath = path + updateDirectoryRequest.getFromPath();
        String toPath = path + updateDirectoryRequest.getToPath();

        File fromDirectory = new File(fromPath);
        File toDirectory = new File(toPath);

        if (fromDirectory.exists()) {

            Directory directory = directoryRepository.findByPath(fromPath)
                    .orElseThrow(() -> new EntityNotFoundException("Directory not found."));

            directoryRepository.save(directory.updateDirectory(toPath));
            fromDirectory.renameTo(toDirectory);

            return "폴더 수정 완료";
        } else {
            return "폴더 수정 불가";
        }
    }

}
