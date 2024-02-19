package everyide.webide.terminal.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerminalExecuteResponseDto {
    private boolean success;
    private String path;
    private String content;
}
