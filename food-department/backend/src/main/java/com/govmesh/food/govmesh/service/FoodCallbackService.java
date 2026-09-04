package com.govmesh.food.govmesh.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class FoodCallbackService {

    private static final Logger log = LoggerFactory.getLogger(FoodCallbackService.class);

    private final String callbackUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public FoodCallbackService(@Value("${govmesh.callback-url:https://sih-26129-gov-mesh-citizen.vercel.app/api/govmesh/callbacks/department-status}") String callbackUrl) {
        this.callbackUrl = callbackUrl;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public void dispatchStatusCallback(String applicationId,
                                       String correlationId,
                                       Integer requestVersion,
                                       String status,
                                       String acknowledgementId,
                                       String receivedAt,
                                       String validatedAt,
                                       String acceptedAt,
                                       String processingStartedAt,
                                       String completedAt,
                                       String canonicalRequestHash,
                                       String documentHash) {
        CompletableFuture.runAsync(() -> {
            try {
                Map<String, Object> payload = new HashMap<>();
                payload.put("applicationId", applicationId);
                payload.put("correlationId", correlationId != null ? correlationId : "CORR-FOOD-" + applicationId);
                payload.put("requestVersion", requestVersion != null ? requestVersion : 1);
                payload.put("departmentCode", "FOOD");
                payload.put("status", status);
                payload.put("acknowledgementId", acknowledgementId != null ? acknowledgementId : "ACK-FOOD-" + applicationId);
                payload.put("receivedAt", receivedAt);
                payload.put("validatedAt", validatedAt);
                payload.put("acceptedAt", acceptedAt);
                payload.put("processingStartedAt", processingStartedAt);
                payload.put("completedAt", completedAt);
                payload.put("canonicalRequestHash", canonicalRequestHash);
                payload.put("documentHash", documentHash);

                String jsonBody = objectMapper.writeValueAsString(payload);
                log.info("Dispatching Food status callback for {} -> {} to {}", applicationId, status, callbackUrl);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(callbackUrl))
                        .timeout(Duration.ofSeconds(5))
                        .header("Content-Type", "application/json")
                        .header("X-Correlation-ID", correlationId != null ? correlationId : applicationId)
                        .header("X-GovMesh-App-ID", applicationId)
                        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                log.info("Dispatched GovMesh status callback for {} -> {} (HTTP {})", applicationId, status, response.statusCode());
            } catch (Exception e) {
                log.warn("GovMesh callback dispatch skipped/failed for {}: {}", applicationId, e.getMessage());
            }
        });
    }
}
