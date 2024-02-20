package everyide.webide.terminal;

import everyide.webide.container.ContainerRepository;
import everyide.webide.container.domain.Container;
import everyide.webide.terminal.domain.TerminalExecuteRequestDto;
import everyide.webide.terminal.domain.TerminalExecuteResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.AccessDeniedException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TerminalService {

    private final ContainerRepository containerRepository;
//    private final ConcurrentHashMap<String, String> userCurrentDirectories = new ConcurrentHashMap<>();

    public TerminalExecuteResponseDto executeCommand(Long containerId, TerminalExecuteRequestDto requestDto) throws IOException, InterruptedException {
        Container container = containerRepository.findById(containerId)
                .orElseThrow(() -> new EntityNotFoundException("Container Not Found"));
        String path = container.getPath() + requestDto.getPath();

//        // 사용자 세션별로 저장된 현재 작업 디렉토리를 가져옵니다. 기본값은 컨테이너의 기본 경로입니다.
//        String currentDirectory = userCurrentDirectories.getOrDefault(sessionId, containerBasePath);
//        if (command.startsWith("cd ")) {
//            String targetDirectory = command.substring(3); // 'cd' 이후의 문자열을 대상 디렉토리로 추출
//            File newDirectory = new File(currentDirectory, targetDirectory).getCanonicalFile(); // 상대 경로를 고려하여 절대 경로로 변환
//
//            // 새 디렉토리가 컨테이너의 경로 내에 있는지 검증
//            if (newDirectory.getPath().startsWith(containerBasePath) && newDirectory.isDirectory()) {
//                userCurrentDirectories.put(sessionId, newDirectory.getPath());
//                return "Success"; // 'cd' 명령어 처리 후 추가 실행 없이 종료
//            } else {
//                return "Access Denied";
//            }
//        }

        ProcessBuilder builder = new ProcessBuilder("sh", "-c", requestDto.getCommand());
        builder.directory(new File(path)); // 세션별 현재 작업 디렉토리 사용
        Process process = builder.start();

        BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        TerminalExecuteResponseDto responseDto = new TerminalExecuteResponseDto();
        if (process.waitFor() == 0) {
            responseDto.setContent(inputReader.lines().collect(Collectors.joining("\t")));
            responseDto.setSuccess(true);
        } else {
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            responseDto.setContent("ERROR:" + errorReader.lines().collect(Collectors.joining("\t")));
            responseDto.setSuccess(false);
        }
        responseDto.setPath(requestDto.getPath());
        return responseDto;
    }
}