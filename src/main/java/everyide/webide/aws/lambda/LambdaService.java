package everyide.webide.aws.lambda;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import everyide.webide.websocket.compile.domain.CompileRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LambdaService {

    private final AWSLambda awsLambda;
    private final String functionName = "everyide-run";

    public String invokeLambdaFunction(String s3Path, String programmingLanguage) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String payload = objectMapper.writeValueAsString(
                    Map.of(
                            "s3Path", s3Path,
                            "taskDefinition", programmingLanguage
                    )
            );

            InvokeRequest invokeRequest = new InvokeRequest()
                    .withFunctionName(functionName)
                    .withPayload(payload);

            InvokeResult invokeResult = awsLambda.invoke(invokeRequest);

            return new String(invokeResult.getPayload().array(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error(e.getMessage());
            return "error";
        }
    }
}
