package everyide.webide.command;

import everyide.webide.command.domain.CommandRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/command")
public class CommandController {

    private final CommandService commandService;

    @PostMapping
    public ResponseEntity<String> executeCommand(@RequestBody CommandRequest commandRequest) {
        try {
            String result = commandService.executeCommand(commandRequest.getUserId(), commandRequest.getCommand());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error executing command: " + e.getMessage());
        }
    }
}
