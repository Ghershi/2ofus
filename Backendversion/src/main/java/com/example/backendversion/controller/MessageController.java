package com.example.backendversion.controller;

import com.example.backendversion.dto.MessageDTO;
import com.example.backendversion.entity.ConnectionEntity;
import com.example.backendversion.entity.MessageEntity;
import com.example.backendversion.entity.UserEntity;
import com.example.backendversion.repository.ConnectionRepository;
import com.example.backendversion.repository.MessageRepository;
import com.example.backendversion.service.ConnectionService;
import com.example.backendversion.service.MessageService;
import com.example.backendversion.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/journal")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
   private ConnectionService connectionService;

    @Autowired
    private UserService userService;

    @Autowired
   private ConnectionRepository connectionRepository;

    @Autowired private MessageRepository messageRepository;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    public void sendMessage(
            @RequestPart("dto") String dtoJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MessageDTO dto = mapper.readValue(dtoJson, MessageDTO.class);

        // 1. Aktuellen User holen (aus SecurityContext oder Session)
        UserEntity sender = userService.getCurrentUser();

        // 2. Akzeptierte Verbindung holen, in der der Sender ist
        ConnectionEntity connection = connectionRepository.findAcceptedConnectionByUser(sender);
        if (connection == null) {
            throw new RuntimeException("Keine akzeptierte Verbindung für den Benutzer gefunden.");
        }

        // 3. Bild speichern, falls vorhanden
        if (image != null && !image.isEmpty()) {
            String originalFilename = image.getOriginalFilename();
            String filename = System.currentTimeMillis() + "_" + originalFilename;
            Path imagePath = Paths.get("images", filename);

            try {
                Files.createDirectories(imagePath.getParent());
                Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

                String imageUrl = "http://localhost:8080/images/" + filename;
                dto.setImageUrl(imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Fehler beim Speichern des Bildes", e);
            }
        }

        // 4. Nachricht erstellen und speichern
        MessageEntity message = new MessageEntity();
        message.setText(dto.getText());
        message.setImageUrl(dto.getImageUrl());
        message.setTimestamp(LocalDateTime.now());
        message.setSender(sender);
        message.setConnection(connection);

        messageRepository.save(message);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")

    public ResponseEntity<List<MessageDTO>> getMessages() {
        List<MessageDTO> messages = messageService.getMessagesForCurrentUser();
        return ResponseEntity.ok(messages);
    }
}
