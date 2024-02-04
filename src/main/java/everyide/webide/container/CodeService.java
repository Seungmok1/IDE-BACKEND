package everyide.webide.container;

import everyide.webide.container.domain.ContainerRunRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CodeService {

    public String saveFile(ContainerRunRequestDto requestDto) {
        try{
            Path commonPath = Path.of(System.getProperty("user.dir") + "/src/main/java/everyide/webide/user_code");
            String additionalPath =
                    switch (requestDto.getProgrammingLanguage()) {
                        case "java" -> "Main.java";
                        case "javascript" -> "script.js";
                        case "python" -> "script.py";
                        default -> null;
            };

            assert additionalPath != null;
            Path path = Files.write(commonPath.resolve(additionalPath), requestDto.getCode().getBytes(), StandardOpenOption.CREATE);

            return path.toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            return "Error in saving file";
        }
    }


    public String runJava(String path) {
        StringBuilder output = new StringBuilder();
        try {
            String command = "docker run --rm -v " + path + ":/app/Main.java openjdk:11 sh -c 'javac /app/Main.java && java -cp /app Main'";

            output.append(executeCommand(command));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return output.toString();
    }

    public String runJavascript(String path) {
        String output = null;
        try {
            String command = "docker run --rm -v " + path + ":/app/script.js node:14 node app/script.js";

            output =  executeCommand(command);
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
        }
        return output;
    }

    public String runPython(String path) {
        String output = null;
        try {
            String command = "docker run --rm -v " + path + ":/app/script.py python:3.9 python /app/script.py";

            output = executeCommand(command);
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
        }
        return output;
    }

    private String executeCommand(String command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", command);
        Process process = processBuilder.start();

        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        String output = input.lines().collect(Collectors.joining("\n"));
        String err = error.lines().collect(Collectors.joining("\n"));

        int exitVal = process.waitFor();
        if (exitVal == 0) {
            return "Output:\n" + output + "\n";
        } else {
            return "Error:\n" + err + "\n";
        }
    }
}
