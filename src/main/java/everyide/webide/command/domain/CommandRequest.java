package everyide.webide.command.domain;

import lombok.Data;

@Data
public class CommandRequest {
    private String userId;
    private String command;
}
