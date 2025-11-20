package com.example.backendversion.repository;

import com.example.backendversion.entity.ConnectionEntity;
import com.example.backendversion.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

    public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
        List<MessageEntity> findByConnectionOrderByTimestampAsc(ConnectionEntity connection);
    }


