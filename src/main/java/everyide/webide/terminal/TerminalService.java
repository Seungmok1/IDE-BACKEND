package everyide.webide.terminal;

import everyide.webide.container.ContainerRepository;
import everyide.webide.container.domain.Container;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class TerminalService {

    private final ContainerRepository containerRepository;
    private final SimpMessagingTemplate template;

    private final ConcurrentHashMap<String, String> userCurrentDirectories = new ConcurrentHashMap<>();

    public void executeCommand(Long containerId, String command, String sessionId) throws IOException, InterruptedException {
        Container container = containerRepository.findById(containerId)
                .orElseThrow(() -> new EntityNotFoundException("Container Not Found"));
        String containerBasePath = container.getPath();

        // 사용자 세션별로 저장된 현재 작업 디렉토리를 가져옵니다. 기본값은 컨테이너의 기본 경로입니다.
        String currentDirectory = userCurrentDirectories.getOrDefault(sessionId, containerBasePath);
        log.info(" 여기 1 !!");
        if (command.startsWith("cd ")) {
            String targetDirectory = command.substring(3); // 'cd' 이후의 문자열을 대상 디렉토리로 추출
            File newDirectory = new File(currentDirectory, targetDirectory).getCanonicalFile(); // 상대 경로를 고려하여 절대 경로로 변환

            // 새 디렉토리가 컨테이너의 경로 내에 있는지 검증
            if (newDirectory.getPath().startsWith(containerBasePath) && newDirectory.isDirectory()) {
                userCurrentDirectories.put(sessionId, newDirectory.getPath());
            } else {
                template.convertAndSendToUser(sessionId, "/topic/terminal-output/" + containerId, "Access Denied");
            }
            return; // 'cd' 명령어 처리 후 추가 실행 없이 종료
        }

        // 'cd' 명령어가 아닌 경우, 현재 디렉토리에서 명령어 실행
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("sh", "-c", command);
        builder.directory(new File(currentDirectory)); // 세션별 현재 작업 디렉토리 사용
        Process process = builder.start();
        log.info(" 여기 2 !!");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
//            template.convertAndSendToUser(sessionId, "/topic/terminal-output/" + containerId, line);
            template.convertAndSend("/topic/terminal-output/" + containerId, line);
        }
        log.info(" 여기 3 !!");
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder errorMessage = new StringBuilder();
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                errorMessage.append(errorLine).append("\n");
            }
            template.convertAndSendToUser(sessionId, "/topic/terminal-output/" + containerId, "Error: " + errorMessage.toString());
        }
        log.info(" 여기 4 !!");
    }
}

