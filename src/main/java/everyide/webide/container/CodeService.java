package everyide.webide.container;

import everyide.webide.container.domain.CodeRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
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

    public String saveFile(CodeRequestDto requestDto) throws IOException {
        try{
            String path = System.getProperty("user.dir") + "/src/main/java/everyide/webide/user_code/" + requestDto.getRoomId();
            File folder = new File(path);
            if(!folder.exists()) folder.mkdir();

            return Files.write(
                    Path.of(path).resolve(requestDto.getFileName()),
                    requestDto.getCode().getBytes(),
                    StandardOpenOption.CREATE
            ).toString();
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
