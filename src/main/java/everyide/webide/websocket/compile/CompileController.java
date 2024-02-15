package everyide.webide.websocket.compile;

import everyide.webide.aws.lambda.LambdaService;
import everyide.webide.aws.s3.S3Service;
import everyide.webide.websocket.compile.domain.CompileRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class CompileController {

    private final LambdaService lambdaService;
    private final S3Service s3Service;

    @MessageMapping("/room/{roomId}/compile")
    @SendToUser("/room/{roomId}/compile")
    public String compile(CompileRequestDto requestDto, @DestinationVariable String roomId) throws IOException {
        String s3Path = s3Service.uploadS3(requestDto, roomId);

        return lambdaService.invokeLambdaFunction(s3Path, requestDto.getProgrammingLanguage());
    }
}
