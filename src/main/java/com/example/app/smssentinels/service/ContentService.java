package com.example.app.smssentinels.service;

import com.example.app.smssentinels.dto.Content;
import com.example.app.smssentinels.dto.ContentResponse;
import com.example.app.smssentinels.entity.Inbox;
import com.example.app.smssentinels.repository.InboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ContentService {
    private final WebClient webClient;
    private final InboxRepository inboxRepository;

    public ContentService(WebClient webClient, InboxRepository inboxRepository) {
        this.webClient = webClient;
        this.inboxRepository = inboxRepository;
    }

    // Consume contents from server
    public Mono<Void> fetchAndStoreContents() {
        // Create a virtual thread pool for handling concurrent database saves
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

        return webClient.get()
                .uri("content")
                .retrieve()
                .bodyToFlux(ContentResponse.class)
                .flatMap(contentResponse -> {
                    List<Content> contents = contentResponse.getContents();
                    return Flux.fromIterable(contents)
                            .flatMap(content -> Mono.fromRunnable(() -> saveContent(content, executorService)));
                })
                .doFinally(signalType -> executorService.shutdown())
                .then();
    }

    private void saveContent(Content content, ExecutorService executorService) {
        Inbox inbox = new Inbox();
        inbox.setTransactionId(content.getTransactionId());
        inbox.setOperator(content.getOperator());
        inbox.setShortCode(content.getShortCode());
        inbox.setMsisdn(content.getMsisdn());
        inbox.setSms(content.getSms());
        inbox.setKeyword(extractKeyword(content.getSms()));
        inbox.setGameName(extractGameName(content.getSms()));
        inbox.setStatus('N'); // Mark as new
        inbox.setCreatedAt(LocalDateTime.now());

        // Submit the save operation to the executor service
        executorService.submit(() -> {
            // Save the Inbox entity to the database
            inboxRepository.save(inbox);
        });
    }

    private String extractKeyword(String sms) {
        // Implement your logic to extract the keyword
        return sms.split(" ")[0]; // Example extraction
    }

    private String extractGameName(String sms) {
        // Implement your logic to extract the game name
        return sms.split(" ")[1]; // Example extraction
    }
}
