package everyide.webide.websocket.terminal.domain;

import lombok.Getter;

@Getter
public class TerminalExecuteRequestDto {
    private String path;
    private String command;
}
