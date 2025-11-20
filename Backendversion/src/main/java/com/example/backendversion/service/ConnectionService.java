package com.example.backendversion.service;


import com.example.backendversion.dto.ConnectionDTO;
import com.example.backendversion.dto.CountdownDateDTO;
import com.example.backendversion.dto.EmotionStatusDTO;
import com.example.backendversion.dto.UserDTO;
import com.example.backendversion.entity.ConnectionEntity;
import com.example.backendversion.entity.RequestEntity;
import com.example.backendversion.entity.UserEntity;
import com.example.backendversion.repository.ConnectionRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Service
public class ConnectionService {

    @Autowired
    private ConnectionRepository connectionRepository;
    @Autowired
    private UserService userService;


    public boolean isUserConnected(UserEntity user) {
        return connectionRepository.existsAcceptedConnectionByUser(user);
    }

    public ConnectionDTO getConnectionData(UserEntity currentUser) {
        ConnectionEntity connectionEntity = connectionRepository.findAcceptedConnectionByUser(currentUser);

        return new ConnectionDTO(connectionEntity);
    }

    public UserDTO getConnectedUser(UserEntity currentUser) {
        ConnectionEntity connectionEntity = connectionRepository.findAcceptedConnectionByUser(currentUser);
        UserEntity user1 = connectionEntity.getUser1();
        UserEntity user2 = connectionEntity.getUser2();
        if(user1 != currentUser){
            UserDTO connectedUser = new UserDTO(user1);
            return connectedUser;
        } else{
            UserDTO connectedUser = new UserDTO(user2);
            return connectedUser;
        }
    }

    public void createConnection(RequestEntity requestEntity) {
        connectionRepository.save(new ConnectionEntity(requestEntity));
    }

    public String triggerEmotion(String emotionType) {
        UserEntity user = userService.getCurrentUser();
        List<ConnectionEntity> connections = connectionRepository.findAcceptedConnectionsByUser(user);

        if (!connections.isEmpty()) {
            ConnectionEntity connection = connections.get(0); // Erste akzeptierte Verbindung
            connection.setLastEmotionTriggered(emotionType);

            LocalDateTime now = LocalDateTime.now();
            connection.setLastEmotionTriggeredAt(now);

            connectionRepository.save(connection);

            return now.toString(); // Oder Formatierung falls nötig
        } else {
            throw new RuntimeException("No accepted connection found for user.");
        }
    }

    public EmotionStatusDTO getEmotionStatus() {
        UserEntity user = userService.getCurrentUser();
        List<ConnectionEntity> connections = connectionRepository.findAcceptedConnectionsByUser(user);

        if (!connections.isEmpty()) {
            ConnectionEntity connection = connections.get(0); // Erste akzeptierte Verbindung
            EmotionStatusDTO dto = new EmotionStatusDTO();
            dto.setType(connection.getLastEmotionTriggered());
            dto.setTriggeredAt(connection.getLastEmotionTriggeredAt());
            return dto;
        } else {
            return new EmotionStatusDTO(); // leeres DTO zurückgeben
        }
    }

    }