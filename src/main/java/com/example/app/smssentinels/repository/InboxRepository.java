package com.example.app.smssentinels.repository;

import com.example.app.smssentinels.entity.Inbox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboxRepository extends JpaRepository<Inbox, Long> {
}
