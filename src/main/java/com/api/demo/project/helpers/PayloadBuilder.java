package com.api.demo.project.helpers;

import com.api.demo.project.annotation.LazyComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
public class PayloadBuilder {

    @Value("${PAYLOAD_PATH}")
    private String PAYLOAD_PATH;

    public String prepareRequestPayload(String payloadFile) {
        String payload = "";
        String payloadRequestPathString = PAYLOAD_PATH + payloadFile;

        try {
            Path payloadRequestPath = Paths.get(payloadRequestPathString);

            if (Files.exists(payloadRequestPath)) {
                payload = new String(Files.readAllBytes(payloadRequestPath));
            } else {
                log.error("File not found: {}", payloadRequestPathString);
            }
        } catch (IOException e) {
            log.error("Error reading file: {}", payloadRequestPathString);
        }
        return payload;
    }

}