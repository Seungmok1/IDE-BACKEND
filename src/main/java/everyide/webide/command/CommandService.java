package everyide.webide.command;

import everyide.webide.user.UserRepository;
import everyide.webide.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommandService {
    private final UserRepository userRepository;

    public String executeCommand(String userId, String command) throws Exception {
        User user = userRepository.findByEmail(userId).orElseThrow(() -> new IllegalArgumentException("User not found" + userId));

        // 경로 검증 로직
        if (!commandIsValid(command, user.getRootPath())) {
//            throw new SecurityException("Access denied. Command attempts to access outside of root directory.");

            return "Root Directory 범위를 벗어날수 없습니다.";
        }

        // 명령어 실행 로직
        log.info(user.getRootPath());
        return executeLocalCommand(command, user.getRootPath());
    }

    private boolean commandIsValid(String command, String rootPath) {
        // "cd" 명령어가 포함되어 있는지 확인하고, ".."을 사용하여 상위 디렉토리로 이동하는 것을 차단합니다.
        if (command.contains("..")) {
            return false; // 상위 디렉토리로의 이동 시도 차단
        }

        // 절대 경로를 사용하는 경우, rootPath로 시작하는지 검증합니다.
        if (command.startsWith("/")) {
            return command.startsWith(rootPath);
        }

        return true; // 위의 조건에 해당하지 않으면 명령어는 유효한 것으로 간주합니다.
    }


    private String executeLocalCommand(String command, String rootPath) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(rootPath)); // 명령어를 실행할 디렉토리 설정

        // 명령어를 OS 별로 처리
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            processBuilder.command("cmd.exe", "/c", command);
        } else {
            processBuilder.command("sh", "-c", command);
        }

        Process process;
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            return "명령어 실행 중 오류가 발생했습니다: " + e.getMessage();
        }

        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                return output.toString();
            } else {
                // 명령어 실행 실패시 에러 스트림의 내용을 반환
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder errorOutput = new StringBuilder();
                while ((line = errorReader.readLine()) != null) {
                    errorOutput.append(line).append("\n");
                }
                return errorOutput.toString(); // 에러 메시지 대신 사용자 친화적인 메시지 반환
            }
        } catch (IOException | InterruptedException e) {
            return "명령어 처리 중 오류가 발생했습니다: " + e.getMessage();
        }
    }


}
