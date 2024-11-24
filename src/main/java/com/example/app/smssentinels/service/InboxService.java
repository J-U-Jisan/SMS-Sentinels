package com.example.app.smssentinels.service;

import com.example.app.smssentinels.dto.Content;
import com.example.app.smssentinels.entity.Inbox;
import com.example.app.smssentinels.repository.InboxRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InboxService {
    private final InboxRepository inboxRepository;

    public InboxService(InboxRepository inboxRepository) {
        this.inboxRepository = inboxRepository;
    }

    @Transactional
    public void saveContents(List<Content> contents) {
        List<Inbox> inboxEntities = contents.parallelStream().map(content -> {
            Inbox inbox = new Inbox();
            inbox.setTransactionId(content.getTransactionId());
            inbox.setOperator(content.getOperator());
            inbox.setShortCode(content.getShortCode());
            inbox.setMsisdn(content.getMsisdn());
            inbox.setSms(content.getSms());
            inbox.setCreatedAt(LocalDateTime.now());

            String[] smsParts = content.getSms().split(" ");
            if (smsParts.length >= 2) {
                inbox.setKeyword(smsParts[0]);
                inbox.setGameName(smsParts[1]);
            } else {
                inbox.setKeyword(null);
                inbox.setGameName(null);
            }

            inbox.setStatus('N'); // Mark as new
            return inbox;
        }).toList();

        // Save all entities in bulk
        inboxRepository.saveAll(inboxEntities);
    }
}
