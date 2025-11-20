package com.example.backendversion.service;

import com.example.backendversion.dto.MessageDTO;
import com.example.backendversion.entity.ConnectionEntity;
import com.example.backendversion.entity.MessageEntity;
import com.example.backendversion.entity.UserEntity;
import com.example.backendversion.repository.ConnectionRepository;
import com.example.backendversion.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ConnectionRepository connectionRepository;
    @Autowired
    private UserService userService;


    public void sendMessage(MessageDTO dto, UserEntity sender, ConnectionEntity connection) {
        MessageEntity message = new MessageEntity();
        message.setText(dto.getText());
        message.setImageUrl(dto.getImageUrl());
        message.setTimestamp(LocalDateTime.now());
        message.setSender(sender);
        message.setConnection(connection);

        messageRepository.save(message);
    }

    public List<MessageDTO> getMessagesForCurrentUser() {
        UserEntity user = userService.getCurrentUser();
        ConnectionEntity connection = connectionRepository.findAcceptedConnectionByUser(user);
        List<MessageEntity> messageEntities = messageRepository.findByConnectionOrderByTimestampAsc(connection);

        List<MessageDTO> messageDTOs = new ArrayList<>();

        for (MessageEntity entity : messageEntities) {
            MessageDTO dto = new MessageDTO();
            dto.setText(entity.getText());
            dto.setImageUrl(entity.getImageUrl());
            dto.setSenderEmail(entity.getSender().getUserEmail());
            dto.setTimestamp(entity.getTimestamp());

            messageDTOs.add(dto);
        }

        return messageDTOs;
    }
}
